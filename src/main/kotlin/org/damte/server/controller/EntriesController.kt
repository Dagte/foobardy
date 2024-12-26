package org.damte.org.damte.server.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.model.DailyEntry
import org.damte.org.damte.server.model.request.DailyEntryRequest
import org.damte.org.damte.server.util.toDailyEntry
import org.koin.ktor.ext.inject

fun Route.entriesRoutes() {
    val storageManager by inject<StorageManager>()
    val json = Json {
        ignoreUnknownKeys = true
    }


    route("/entries") {
        // Get all entries
        get {
            val entries = storageManager.loadEntries()
            call.respond(entries)
        }

        // Create a new entry
        post {
            val contentType = call.request.contentType()
            println("Received Content-Type: $contentType")
            val bodyText = call.receiveText()
            println("Received body: $bodyText")

            try {
                val entry = json.decodeFromString<DailyEntryRequest>(bodyText)
                println("Parsed entry: $entry")

                val createdEntry = storageManager.saveEntries(listOf(entry.toDailyEntry()))
                call.respond(HttpStatusCode.Created, createdEntry)
            } catch (e: SerializationException) {
                println("Error parsing JSON: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }

        // Additional routes (GET by ID, PUT, DELETE) can be added here
    }
}
