package org.damte.org.damte.server.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import org.damte.org.damte.server.model.DailyEntry

fun Route.dailyEntryRoutes() {
    route("/entries") {
        get {
            // Example: Return a static list of entries
            call.respond(
                listOf(
                    DailyEntry(LocalDate.parse("2024-11-24"), "Happy", 8.0, "Toast", "Salad", "Pasta")
                )
            )
        }
        post {
            call.respond(HttpStatusCode.Created, "Entry created!")
        }
    }
}
