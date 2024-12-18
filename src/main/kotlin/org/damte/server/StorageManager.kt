package org.damte.org.damte.server

import org.damte.org.damte.server.model.DailyEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object StorageManager {
    private const val DEFAULT_FILENAME = "entries.json"
    private val json = Json { prettyPrint = true }


    fun saveEntries(entries: List<DailyEntry>) {
        try {
            val jsonString = Json.encodeToString(entries)
            File(DEFAULT_FILENAME).writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving entries: ${e.message}")
        }
    }

    fun addEntries(entries: List<DailyEntry>, filename: String = DEFAULT_FILENAME) {
        try {
            val file = File(filename)
            val existingEntries = getCurrentEntries(file)

            existingEntries.addAll(entries)

            val jsonString = Json.encodeToString(existingEntries)
            file.writeText(jsonString)
            println("Successfully added ${entries.size} entries.")
        } catch (e: Exception) {
            println("Error saving entries: ${e.message}")
        }
    }

    fun loadEntries(): MutableList<DailyEntry> {
        return try {
            getCurrentEntries(File(DEFAULT_FILENAME))
        } catch (e: Exception) {
            println("Error loading entries: ${e.message}")
            mutableListOf()
        }
    }

    private fun getCurrentEntries(file: File): MutableList<DailyEntry> {
        val existingEntries = if (file.exists() && file.readText().isNotBlank()) {
            Json.decodeFromString<List<DailyEntry>>(file.readText())
        } else {
            mutableListOf()
        }
        return existingEntries.toMutableList()
    }
}
