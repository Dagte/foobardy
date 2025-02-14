package org.damte.server


import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.damte.org.damte.server.controller.entriesRoutes
import org.damte.server.auth.authRoutes
import org.damte.server.auth.validateAuthentication
import org.damte.server.di.appModule
import org.damte.server.model.Session
import org.damte.server.model.UserCredentials
import org.damte.server.plugins.configureSessions
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    install(ContentNegotiation) {
        json()
    }

    configureSessions()

    routing {
//        rootRoutes()

        authRoutes()

        entriesRoutes()
    }

}
