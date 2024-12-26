package org.damte.org.damte.server.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.model.DailyEntry
import org.koin.ktor.ext.inject

fun Route.entriesRoutes() {
    val storageManager by inject<StorageManager>()

    route("/entries") {
        // Get all entries
        get {
            val entries = storageManager.loadEntries()
            call.respond(entries)
        }

        // Create a new entry
        post {
            val entry = call.receive<DailyEntry>()
            val createdEntry = storageManager.saveEntries(listOf(entry))
            call.respond(HttpStatusCode.Created, createdEntry)
        }

        // Additional routes (GET by ID, PUT, DELETE) can be added here
    }
}
