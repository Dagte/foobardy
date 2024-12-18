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
import org.damte.org.damte.server.StorageManager.addEntries
import org.damte.org.damte.server.model.request.DailyEntryRequest
import org.damte.org.damte.server.StorageManager.loadEntries
import org.damte.org.damte.server.util.toDailyEntry

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
                get {
                    val entries = loadEntries()
                    call.respond(entries)
                }

                post {
                    try {
                        val entry = call.receive<DailyEntryRequest>()
                        val createdEntry = addEntries(listOf(entry.toDailyEntry()))
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