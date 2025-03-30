package org.damte.server

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.damte.org.damte.server.controller.entriesRoutes
import org.damte.server.auth.authRoutes
import org.damte.server.plugins.configureSessions
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.damte.server.controller.adminRoutes
import org.damte.server.di.AppModule
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Application::class.java)

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    logger.info("Starting application initialization")
    
    install(Koin) {
        logger.info("Setting up Koin dependency injection")
        slf4jLogger()
        modules(AppModule)
        logger.info("Koin setup completed")
    }

    install(ContentNegotiation) {
        logger.info("Configuring content negotiation")
        json()
    }

    configureSessions()
    logger.info("Sessions configured")

    routing {
        logger.info("Setting up routes")
        authRoutes()
        entriesRoutes()
        adminRoutes()
        logger.info("Routes setup completed")
    }
    
    logger.info("Application initialization completed")
}
