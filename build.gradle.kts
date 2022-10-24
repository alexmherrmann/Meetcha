plugins {
    id("java")
    kotlin("jvm") version "1.7.20"
}

group = "com.alexmherrmann"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hamcrest:hamcrest:2.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}