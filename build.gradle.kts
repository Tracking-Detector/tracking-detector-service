import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.10"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "11.2.0"
    id("com.bmuschko.docker-spring-boot-application") version "9.3.1"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.tracking-detector"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    google()
}
extra["testcontainersVersion"] = "1.17.6"

dependencies {
    implementation("org.apache.xmlrpc:xmlrpc-client:3.1.3")
    implementation("org.asynchttpclient:async-http-client:2.12.3")
    implementation("io.github.oshai:kotlin-logging-jvm:4.0.0-beta-22")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-webmvc:6.0.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlin-deeplearning-tensorflow:0.5.1")
    implementation("io.minio:minio:8.5.2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.apache.xmlrpc:xmlrpc-server:3.1.3")
    testImplementation("org.openrefine:jython:3.7.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:mongodb:1.16.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("org.mockito:mockito-core:2.23.0")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
}

ktlint {
    debug.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    android.set(false)
    verbose.set(false)
    enableExperimentalRules.set(false)
}

docker {
    springBootApplication {
        baseImage.set("openjdk:17-jdk-alpine")
        ports.set(listOf(3000))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
