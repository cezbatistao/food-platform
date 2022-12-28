import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("kapt") version "1.5.10"
}

group = "com.food.review"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2021.0.5"
extra["kotlinLoggingJvmVersion"] = "3.0.4"
extra["mapstructVersion"] = "1.5.3.Final"
extra["testcontainersVersion"] = "1.16.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude("org.springframework.boot", "spring-boot-starter-tomcat")
	}
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8-config")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.kafka:spring-kafka")

	implementation("com.google.guava:guava:31.1-jre")
	implementation("net.logstash.logback:logstash-logback-encoder:7.2")
	implementation("io.github.microutils:kotlin-logging-jvm:${property("kotlinLoggingJvmVersion")}")
	implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
	kapt("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

	testImplementation("org.assertj:assertj-core:3.23.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude("org.junit.vintage", "junit-vintage-engine")
		exclude("org.mockito", "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:4.0.0")
	testImplementation("org.testcontainers:mongodb")
	testImplementation("org.testcontainers:kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
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
