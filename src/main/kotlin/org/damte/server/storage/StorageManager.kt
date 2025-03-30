package org.damte.org.damte.server

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.damte.org.damte.server.model.DailyEntry
import org.damte.server.model.dto.UpdateDailyEntry
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

    fun updateEntry(updateDailyEntry: UpdateDailyEntry) {
        try {
            val file = File(filename)
            val existingEntries = getCurrentEntries(file)

            val updatedEntries = existingEntries.map { existing ->
                if (existing.date == updateDailyEntry.date) existing.copy(
                    date = updateDailyEntry.date,
                    mood = updateDailyEntry.mood?.takeIf { newMood -> newMood != existing.mood } ?: existing.mood,
                    sleepHours = updateDailyEntry.sleepHours?.takeIf { newSleep -> newSleep != existing.sleepHours }
                        ?: existing.sleepHours,
                    breakfast = updateDailyEntry.breakfast?.takeIf { newBreakfast -> newBreakfast != existing.breakfast }
                        ?: existing.breakfast,
                    lunch = updateDailyEntry.lunch?.takeIf { newLunch -> newLunch != existing.lunch } ?: existing.lunch,
                    dinner = updateDailyEntry.dinner?.takeIf { newDinner -> newDinner != existing.dinner }
                        ?: existing.dinner,
                    lactose = updateDailyEntry.lactose?.takeIf { newLactose -> newLactose != existing.lactose }
                        ?: existing.lactose,
                    gluten = updateDailyEntry.gluten?.takeIf { newLactose -> newLactose != existing.gluten }
                        ?: existing.gluten,
                )
                else existing
            }

            val listWasUpdated = updatedEntries != existingEntries

            if (listWasUpdated) {
                val jsonString = Json.encodeToString(updatedEntries)
                file.writeText(jsonString)
                println("Entry was updated")
            }
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
