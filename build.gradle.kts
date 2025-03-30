val ktor_version = "3.0.1"
val koin_version = "3.4.0"

plugins {
    kotlin("jvm") version "2.0.21"
    application
    kotlin("plugin.serialization") version "2.0.21"
    id("io.ktor.plugin") version "3.0.1"
}

application {
    mainClass.set("org.damte.server.ApplicationKt")
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}

group = "org.damte"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // Ktor dependencies
    implementation("io.ktor:ktor-server-core:${ktor_version}")
    implementation("io.ktor:ktor-server-netty:${ktor_version}")
    implementation("io.ktor:ktor-server-sessions:${ktor_version}") // For session handling
    implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
    implementation("io.ktor:ktor-server-auth:${ktor_version}") // For authentication support
    implementation("io.ktor:ktor-server-config-yaml:${ktor_version}")
    implementation("io.ktor:ktor-server-status-pages:${ktor_version}")


    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:${koin_version}")
    implementation("io.insert-koin:koin-logger-slf4j:${koin_version}")

    // Exposed dependencies
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.41.1")

    // SQLite Database
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Other
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests:2.3.1")

}

application {
    mainClass.set("org.damte.server.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}