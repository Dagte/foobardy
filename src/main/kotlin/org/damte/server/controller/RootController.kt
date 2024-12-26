package org.damte.org.damte.server.controller

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.rootRoutes() {
    get("/") {
        call.respondText("Hello, foo!")
    }
}