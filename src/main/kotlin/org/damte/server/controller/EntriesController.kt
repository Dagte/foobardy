package org.damte.org.damte.server.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.model.request.DailyEntryCreateRequest
import org.damte.org.damte.server.model.request.DailyEntryUpdateRequest
import org.damte.org.damte.server.util.toDailyEntry
import org.damte.org.damte.server.util.toUpdateDailyEntry
import org.damte.server.auth.validateAuthentication
import org.damte.server.model.request.EntryQueryParams
import org.koin.ktor.ext.inject

fun Route.entriesRoutes() {
    val storageManager by inject<StorageManager>()
    val json = Json {
        ignoreUnknownKeys = true
    }

    route("/entries") {
        get {
            validateAuthentication()

            try {
                val queryParams = EntryQueryParams(
                    startDate = call.parameters["startDate"]?.let {
                        try {
                            LocalDate.parse(it)
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid startDate format. Use: yyyy-MM-dd")
                            return@get
                        }
                    },
                    endDate = call.parameters["endDate"]?.let {
                        try {
                            LocalDate.parse(it)
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid endDate format. Use: yyyy-MM-dd")
                            return@get
                        }
                    },
                    page = call.parameters["page"]?.toIntOrNull() ?: 1,
                    pageSize = call.parameters["pageSize"]?.toIntOrNull() ?: 20,
                    sortBy = call.parameters["sortBy"] ?: "date",
                    sortOrder = call.parameters["sortOrder"]?.lowercase() ?: "desc"
                )

                val validationError = validateEntriesQueryParams(queryParams)
                if (validationError != null) {
                    call.respond(HttpStatusCode.BadRequest, validationError)
                    return@get
                }

                val response = storageManager.loadEntries(queryParams)
                call.respond(response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to process request: ${e.message}")
                )
            }
        }

        post {
            validateAuthentication()
            val bodyText = call.receiveText()
            try {
                val entry = json.decodeFromString<DailyEntryCreateRequest>(bodyText)
                val result = storageManager.updateEntry(entry.toDailyEntry())
                call.respond(HttpStatusCode.Created, result)
            } catch (e: SerializationException) {
                ("Error parsing JSON: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }

        put {
            validateAuthentication()
            val bodyText = call.receiveText()
            try {
                val entry = json.decodeFromString<DailyEntryUpdateRequest>(bodyText)
                val result = storageManager.updateEntry(entry.toUpdateDailyEntry())
                call.respond(HttpStatusCode.Accepted, result)
            } catch (e: SerializationException) {
                ("Error parsing JSON: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }

        // Additional routes (GET by ID, PUT, DELETE) can be added here
    }
}

private fun validateEntriesQueryParams(queryParams: EntryQueryParams): String? {
    if (queryParams.page < 1) {
        return "Page number must be greater than 0"
    }
    if (queryParams.pageSize < 1 || queryParams.pageSize > 100) {
        return "Page size must be between 1 and 100"
    }
    if (!listOf("date", "mood", "sleepHours").contains(queryParams.sortBy)) {
        return "Invalid sort field"
    }
    if (!listOf("asc", "desc").contains(queryParams.sortOrder)) {
        return "Sort order must be 'asc' or 'desc'"
    }

    if (queryParams.startDate != null && queryParams.endDate != null) {
        if (queryParams.startDate > queryParams.endDate) {
            return "startDate cannot be after endDate"
        }
    }
    return null
}
