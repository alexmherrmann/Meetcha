import org.jetbrains.kotlin.load.kotlin.signatures

plugins {
    id("java")
    kotlin("jvm") version "1.7.20"
    id("signing")
//    id("maven")
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

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from("javadoc")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

//signing {
//    configurations.get("sources")
//}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

tasks.test {
    useJUnitPlatform()
}