package org.damte.org.damte.server

import org.damte.org.damte.server.model.DailyEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class StorageManager(private val filename: String = "entries.json") {
    private val json = Json { prettyPrint = true }


    fun saveEntries(entries: List<DailyEntry>) {
        try {
            val jsonString = Json.encodeToString(entries)
            File(filename).writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving entries: ${e.message}")
        }
    }

    fun addEntries(entries: List<DailyEntry>) {
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

    fun updateEntry(entry: DailyEntry) {
        try {
            val file = File(filename)
            val existingEntries = getCurrentEntries(file)

            val updatedEntries = existingEntries.map { if (it.date == entry.date) entry else it }
            val listWasUpdated = updatedEntries != existingEntries

            if (existingEntries.isEmpty() || !listWasUpdated) {
                existingEntries.add(entry)
            }
            val entriesToBeAdded = if (listWasUpdated) updatedEntries else existingEntries
            val jsonString = Json.encodeToString(entriesToBeAdded)

            file.writeText(jsonString)
            println("Entry was ${if (listWasUpdated) "updated" else "added"}")
        } catch (e: Exception) {
            println("Error saving entries: ${e.message}")
        }
    }

    fun loadEntries(): MutableList<DailyEntry> {
        return try {
            getCurrentEntries(File(filename))
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
