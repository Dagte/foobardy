package org.damte.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.damte.org.damte.server.StorageManager.loadEntries
import org.damte.org.damte.server.StorageManager.saveEntries
import org.damte.org.damte.server.model.DailyEntry

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello, foo!")
        }

        route("/entries") {
            // Get all entries
            get {
                val entries = loadEntries()
                call.respond(entries)
            }

            // Create a new entry
            post {
                val entry = call.receive<DailyEntry>()
                val createdEntry = saveEntries(listOf(entry))
                call.respond(HttpStatusCode.Created, createdEntry)
            }

            // Additional routes (GET by ID, PUT, DELETE) can be added here
        }
    }
}