package org.damte.server.plugins

import io.ktor.server.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.sessions.*
import org.damte.server.model.Session
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.configureSessions() {

    install(Sessions) {
        cookie<Session>("SESSION_ID") {
            cookie.extensions["SameSite"] = "Strict"
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Unknown error")
        }
    }
}
