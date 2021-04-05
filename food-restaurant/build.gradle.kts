import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.com.google.common.collect.ImmutableMap

plugins {
	id("org.springframework.boot") version "2.4.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.asciidoctor.convert") version "1.5.8"
	id("com.palantir.docker") version "0.26.0"
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.31"
	jacoco
}

group = "com.food"
version = "1.0.0-${extra["build.number"]}"
description = "Restaurant microservice description"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

jacoco {
	toolVersion = "0.8.6"
	reportsDirectory.set(file("$buildDir/customJacocoReportDir"))
}

apply(plugin = "com.palantir.docker")

val snippetsDir = file("build/generated-snippets").also { extra["snippetsDir"] = it }
extra["springCloudVersion"] = "2020.0.2"
extra["testcontainersVersion"] = "1.15.2"
extra["sfmSpringjdbcVersion"] = "8.2.3"
extra["springfoxVersion"] = "3.0.0"
extra["springmockkVersion"] = "3.0.1"
extra["mockitoKotlinVersion"] = "2.2.0"
extra["fixtureFactoryVersion"] = "3.1.0"
extra["cucumberVersion"] = "6.10.2"
extra["cucumberReportingVersion"] = "5.5.2"
extra["junitVintageEngineVersion"] = "5.7.1"
extra["commonsIoVersion"] = "2.8.0"

configurations {
	implementation.get().exclude(module= "spring-boot-starter-tomcat")
}

val componentTest by sourceSets.creating {
	java.srcDir(arrayOf("src/test-component/kotlin", "src/main/kotlin"))
	resources.srcDir(arrayOf("src/test-component/resources", "src/main/resources"))

	compileClasspath += sourceSets.main.get().output + configurations.testRuntime.get()
	runtimeClasspath += output + compileClasspath
}

sourceSets {
	test {
		java {
			srcDir("src/test-component/kotlin")
		}
		resources {
			srcDir("src/test-component/resources")
		}
	}
}

configurations[componentTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[componentTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	implementation("org.simpleflatmapper:sfm-springjdbc:${property("sfmSpringjdbcVersion")}")
	implementation("io.springfox:springfox-swagger2:${property("springfoxVersion")}")
	implementation("io.springfox:springfox-swagger-ui:${property("springfoxVersion")}")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	runtimeOnly("mysql:mysql-connector-java")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("com.ninja-squad:springmockk:${property("springmockkVersion")}")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${property("mockitoKotlinVersion")}")
	testImplementation("org.testcontainers:mysql")
	testImplementation("br.com.six2six:fixture-factory:${property("fixtureFactoryVersion")}")

	testImplementation("io.cucumber:cucumber-java:${property("cucumberVersion")}")
	testImplementation("io.cucumber:cucumber-junit:${property("cucumberVersion")}")
	testImplementation("io.cucumber:cucumber-spring:${property("cucumberVersion")}")
	testImplementation("net.masterthought:cucumber-reporting:${property("cucumberReportingVersion")}")
	testImplementation("commons-io:commons-io:${property("commonsIoVersion")}")

	"componentTestImplementation"(project)
	"componentTestImplementation"("org.junit.vintage:junit-vintage-engine:${property("junitVintageEngineVersion")}")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

val componentTestTask = task<Test>("componentTest") {
	description = "Runs component tests."
	group = "verification"

	testClassesDirs = componentTest.output.classesDirs
	classpath = configurations[componentTest.runtimeClasspathConfigurationName] + componentTest.output

	systemProperty("cucumber.publish.quiet", "true")

	shouldRunAfter(tasks.test)
}

tasks.check {
	dependsOn(componentTestTask)
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(snippetsDir)
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.isEnabled = false
		csv.isEnabled = false
		html.isEnabled = true
		html.destination = file("$buildDir/reports/coverage")
	}
	dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.withType<JacocoCoverageVerification> {
	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).apply {
				exclude("**/config/**")
				exclude("**/domain/**")
				exclude("**/model/**")
				exclude("**/RestaurantApplicationKt.class")
			}
		}))
	}
}
tasks.withType<JacocoReport> {
	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).apply {
				exclude("**/config/**")
				exclude("**/domain/**")
				exclude("**/model/**")
				exclude("**/RestaurantApplicationKt.class")
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
	buildArgs(ImmutableMap.of("name", "${project.name}"))
	copySpec.from("build").into("build")
	pull(true)
	setDockerfile(file("Dockerfile"))
	noCache(true)
}
