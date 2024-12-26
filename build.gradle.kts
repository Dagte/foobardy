plugins {
    kotlin("jvm") version "2.0.21"
    application
//    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    kotlin("plugin.serialization") version "2.0.21"
    id("io.ktor.plugin") version "3.0.1"
}

group = "org.damte"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // Ktor dependencies
    implementation("io.ktor:ktor-server-core:3.0.1")
    implementation("io.ktor:ktor-server-netty:3.0.1")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.1")
    implementation("io.ktor:ktor-server-config-yaml:3.0.1")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:4.1.0-Beta1")
    implementation("io.insert-koin:koin-logger-slf4j:4.1.0-Beta1")

    // Exposed dependencies
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

    // H2 Database
    implementation("com.h2database:h2:2.2.220")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Other
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

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