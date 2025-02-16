package org.damte.server.auth

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.damte.server.model.Session
import org.damte.server.model.UserCredentials

fun Route.authRoutes() {
    route("/login") {
        post {
            val userCredentials = call.receive<UserCredentials>()
            if (AuthService.validateCredentials(userCredentials)) {
                call.sessions.set(Session(userCredentials.username))
                call.respond(HttpStatusCode.OK, mapOf("message" to "Login successful"))
            } else {
                call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Invalid credentials"))
            }
        }
    }

    route("/health") {
        get {
            validateAuthentication()
            val session = call.sessions.get<Session>()
            call.respond(HttpStatusCode.OK, "Hello ${session?.userId}, you are authenticated!")
        }
    }

    route("/logout") {
        post {
            call.sessions.clear<Session>()
            call.respond(HttpStatusCode.OK, "Logged out")
        }
    }
}
