package org.damte.server.auth

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.damte.server.model.Session


suspend fun RoutingContext.validateAuthentication() {
    val session = call.sessions.get<Session>()
    if (session == null) {
        call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "You are not logged in"))
    }
}