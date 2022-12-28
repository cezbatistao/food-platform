import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("org.asciidoctor.convert") version "1.5.8"
	id("com.palantir.docker") version "0.26.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("kapt") version "1.5.10"
	jacoco
}

group = "com.food.review"
version = "1.0.0-${extra["build.number"]}"
description = "Review microservice description"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

jacoco {
	toolVersion = "0.8.8"
	reportsDirectory.set(file("$buildDir/customJacocoReportDir"))
}

apply(plugin = "com.palantir.docker")

val snippetsDir = file("build/generated-snippets").also { extra["snippetsDir"] = it }

extra["springdocOpenapiUiVersion"] = "1.6.14"
extra["kotlinLoggingJvmVersion"] = "3.0.4"
extra["mapstructVersion"] = "1.5.3.Final"
extra["guavaVersion"] = "31.1-jre"
extra["logstashLogbackencoderVersion"] = "7.2"
extra["assertjCoreVersion"] = "3.23.1"
extra["springMockkVersion"] = "4.0.0"

extra["springCloudVersion"] = "2021.0.5"
extra["mongockVersion"] = "5.2.2"
extra["testcontainersVersion"] = "1.16.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude("org.springframework.boot", "spring-boot-starter-tomcat")
	}
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springdoc:springdoc-openapi-ui:${property("springdocOpenapiUiVersion")}")
	implementation("org.springdoc:springdoc-openapi-kotlin:${property("springdocOpenapiUiVersion")}")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8-config")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.kafka:spring-kafka")

	implementation("io.mongock:mongock-springboot")
	implementation("io.mongock:mongodb-springdata-v3-driver")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.google.guava:guava:${property("guavaVersion")}")
	implementation("net.logstash.logback:logstash-logback-encoder:${property("logstashLogbackencoderVersion")}")
	implementation("io.github.microutils:kotlin-logging-jvm:${property("kotlinLoggingJvmVersion")}")
	implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
	kapt("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

	testImplementation("org.assertj:assertj-core:${property("assertjCoreVersion")}")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude("org.junit.vintage", "junit-vintage-engine")
		exclude("org.mockito", "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.instancio:instancio-junit:2.0.0")
	testImplementation("com.ninja-squad:springmockk:${property("springMockkVersion")}")
	testImplementation("org.testcontainers:mongodb")
	testImplementation("org.testcontainers:kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		mavenBom("io.mongock:mongock-bom:${property("mongockVersion")}")
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		sourceCompatibility = "17"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

kapt {
	arguments {
		// Set Mapstruct Configuration options here
		// https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
		// https://mapstruct.org/documentation/stable/reference/html/#configuration-options
		// arg("mapstruct.defaultComponentModel", "spring")
	}
}

tasks.test {
	outputs.dir(snippetsDir)
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("$buildDir/reports/coverage"))
	}
	dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.withType<JacocoCoverageVerification> {
	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).apply {
				exclude("**/config/**")
				exclude("**/json/**")
				exclude("**/domain/**")
				exclude("**/entity/**")
				exclude("**/resource/**")
				exclude("**/mapper/**")
				exclude("**/FoodReviewApplicationKt.class")
			}
		}))
	}
}
tasks.withType<JacocoReport> {
	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).apply {
				exclude("**/config/**")
				exclude("**/json/**")
				exclude("**/domain/**")
				exclude("**/entity/**")
				exclude("**/resource/**")
				exclude("**/mapper/**")
				exclude("**/FoodReviewApplicationKt.class")
			}
		}))
	}
}

tasks.asciidoctor {
	inputs.dir(snippetsDir)
	dependsOn(tasks.test)
}

tasks.processResources {
	filesMatching("**/application.yml") {
		expand(project.properties)
	}
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}

docker {
	name = "${project.name}:".plus(version)
	tag("DockerHub", "cezbatistao/${project.name}:".plus(version))
	buildArgs(org.jetbrains.kotlin.com.google.common.collect.ImmutableMap.of("name", "${project.name}"))
	copySpec.from("build").into("build")
	pull(true)
	setDockerfile(file("Dockerfile"))
	noCache(true)
}
