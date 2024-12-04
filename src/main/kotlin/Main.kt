package org.damte

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.damte.org.damte.server.model.DailyEntry
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.ConsoleUI
import org.damte.org.damte.server.StorageManager.loadEntries
import org.damte.org.damte.server.StorageManager.saveEntries

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }

            route("/entries") {
                // Get all entries
                get {
                    val entries = loadEntries()
                    call.respond(entries)
                }

                // Create a new entry
                post {
                    try {
                        val entry = call.receive<DailyEntry>()
                        val createdEntry = saveEntries(listOf(entry))
                        call.respond(HttpStatusCode.Created, createdEntry)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid data")
                    }
                }

                // Additional routes (GET by ID, PUT, DELETE) can be added here
            }
        }
    }.start(wait = true)
}