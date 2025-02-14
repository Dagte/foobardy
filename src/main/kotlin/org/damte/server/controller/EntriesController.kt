package org.damte.org.damte.server.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.model.request.DailyEntryRequest
import org.damte.org.damte.server.util.toDailyEntry
import org.damte.server.auth.validateAuthentication
import org.koin.ktor.ext.inject

fun Route.entriesRoutes() {
    val storageManager by inject<StorageManager>()
    val json = Json {
        ignoreUnknownKeys = true
    }


    route("/entries") {
        get {
            validateAuthentication()

            val entries = storageManager.loadEntries()
            call.respond(entries)
        }

        post {
            validateAuthentication()
            val bodyText = call.receiveText()
            try {
                val entry = json.decodeFromString<DailyEntryRequest>(bodyText)
                val result = storageManager.updateEntry(entry.toDailyEntry())
                call.respond(HttpStatusCode.Created, result)
            } catch (e: SerializationException) {
                ("Error parsing JSON: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }

        // Additional routes (GET by ID, PUT, DELETE) can be added here
    }
}
