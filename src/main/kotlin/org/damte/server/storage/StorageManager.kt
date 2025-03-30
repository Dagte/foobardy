package org.damte.org.damte.server

import org.damte.org.damte.server.model.DailyEntry
import org.damte.server.model.dto.UpdateDailyEntry
import org.damte.server.database.DailyEntryRepository
import org.slf4j.LoggerFactory
import kotlinx.datetime.LocalDate
// import kotlinx.serialization.encodeToString
// import kotlinx.serialization.json.Json
// import java.io.File

class StorageManager {
    private val logger = LoggerFactory.getLogger(StorageManager::class.java)
    // private val json = Json { prettyPrint = true }
    // private val filename = "entries.json.migrated" // Using the migrated file as reference

    fun saveEntries(entries: List<DailyEntry>) {
        try {
            entries.forEach { entry ->
                DailyEntryRepository.insertEntry(
                    date = entry.date,
                    mood = entry.mood,
                    sleepHours = entry.sleepHours,
                    breakfast = entry.breakfast,
                    lunch = entry.lunch,
                    dinner = entry.dinner,
                    lactose = entry.lactose,
                    gluten = entry.gluten
                )
            }
            logger.info("Successfully saved ${entries.size} entries")
        } catch (e: Exception) {
            logger.error("Error saving entries: ${e.message}", e)
            throw e
        }

        // Old JSON implementation:
        // try {
        //     val jsonString = Json.encodeToString(entries)
        //     File(filename).writeText(jsonString)
        // } catch (e: Exception) {
        //     println("Error saving entries: ${e.message}")
        // }
    }

    fun addEntries(entries: List<DailyEntry>) {
        try {
            entries.forEach { entry ->
                DailyEntryRepository.insertEntry(
                    date = entry.date,
                    mood = entry.mood,
                    sleepHours = entry.sleepHours,
                    breakfast = entry.breakfast,
                    lunch = entry.lunch,
                    dinner = entry.dinner,
                    lactose = entry.lactose,
                    gluten = entry.gluten
                )
            }
            logger.info("Successfully added ${entries.size} entries")
        } catch (e: Exception) {
            logger.error("Error adding entries: ${e.message}", e)
            throw e
        }

        // Old JSON implementation:
        // try {
        //     val file = File(filename)
        //     val existingEntries = getCurrentEntries(file)
        //     existingEntries.addAll(entries)
        //     val jsonString = Json.encodeToString(existingEntries)
        //     file.writeText(jsonString)
        //     println("Successfully added ${entries.size} entries.")
        // } catch (e: Exception) {
        //     println("Error saving entries: ${e.message}")
        // }
    }

    fun updateEntry(entry: DailyEntry) {
        try {
            DailyEntryRepository.insertEntry(
                date = entry.date,
                mood = entry.mood,
                sleepHours = entry.sleepHours,
                breakfast = entry.breakfast,
                lunch = entry.lunch,
                dinner = entry.dinner,
                lactose = entry.lactose,
                gluten = entry.gluten
            )
            logger.info("Successfully updated entry for date ${entry.date}")
        } catch (e: Exception) {
            logger.error("Error updating entry: ${e.message}", e)
            throw e
        }

        // Old JSON implementation:
        // try {
        //     val file = File(filename)
        //     val existingEntries = getCurrentEntries(file)
        //     val updatedEntries = existingEntries.map { if (it.date == entry.date) entry else it }
        //     val listWasUpdated = updatedEntries != existingEntries
        //     if (existingEntries.isEmpty() || !listWasUpdated) {
        //         existingEntries.add(entry)
        //     }
        //     val entriesToBeAdded = if (listWasUpdated) updatedEntries else existingEntries
        //     val jsonString = Json.encodeToString(entriesToBeAdded)
        //     file.writeText(jsonString)
        //     println("Entry was ${if (listWasUpdated) "updated" else "added"}")
        // } catch (e: Exception) {
        //     println("Error saving entries: ${e.message}")
        // }
    }

    fun updateEntry(updateDailyEntry: UpdateDailyEntry) {
        try {
            val entries = DailyEntryRepository.getAllEntries()
            val existingEntry = entries.find { it["date"].toString().equals(updateDailyEntry.date) }

            if (existingEntry != null) {
                DailyEntryRepository.insertEntry(
                    date = updateDailyEntry.date,
                    mood = updateDailyEntry.mood ?: existingEntry["mood"] as String,
                    sleepHours = updateDailyEntry.sleepHours ?: existingEntry["sleepHours"] as Double,
                    breakfast = updateDailyEntry.breakfast ?: existingEntry["breakfast"] as List<String>,
                    lunch = updateDailyEntry.lunch ?: existingEntry["lunch"] as List<String>,
                    dinner = updateDailyEntry.dinner ?: existingEntry["dinner"] as List<String>,
                    lactose = updateDailyEntry.lactose ?: existingEntry["lactose"] as Boolean,
                    gluten = updateDailyEntry.gluten ?: existingEntry["gluten"] as Boolean
                )
                logger.info("Successfully updated entry for date ${updateDailyEntry.date}")
            } else {
                logger.warn("No entry found for date ${updateDailyEntry.date}")
            }
        } catch (e: Exception) {
            logger.error("Error updating entry: ${e.message}", e)
            throw e
        }

        // Old JSON implementation:
        // try {
        //     val file = File(filename)
        //     val existingEntries = getCurrentEntries(file)
        //     val updatedEntries = existingEntries.map { existing ->
        //         if (existing.date == updateDailyEntry.date) existing.copy(
        //             date = updateDailyEntry.date,
        //             mood = updateDailyEntry.mood?.takeIf { it != existing.mood } ?: existing.mood,
        //             sleepHours = updateDailyEntry.sleepHours?.takeIf { it != existing.sleepHours } ?: existing.sleepHours,
        //             breakfast = updateDailyEntry.breakfast?.takeIf { it != existing.breakfast } ?: existing.breakfast,
        //             lunch = updateDailyEntry.lunch?.takeIf { it != existing.lunch } ?: existing.lunch,
        //             dinner = updateDailyEntry.dinner?.takeIf { it != existing.dinner } ?: existing.dinner,
        //             lactose = updateDailyEntry.lactose?.takeIf { it != existing.lactose } ?: existing.lactose,
        //             gluten = updateDailyEntry.gluten?.takeIf { it != existing.gluten } ?: existing.gluten,
        //         )
        //         else existing
        //     }
        //     if (updatedEntries != existingEntries) {
        //         val jsonString = Json.encodeToString(updatedEntries)
        //         file.writeText(jsonString)
        //         println("Entry was updated")
        //     }
        // } catch (e: Exception) {
        //     println("Error saving entries: ${e.message}")
        // }
    }

    fun loadEntries(): List<DailyEntry> {
        return try {
            val entries = DailyEntryRepository.getAllEntries()
            entries.map { entry ->
                DailyEntry(
                    date = LocalDate.parse(entry["date"] as String),
                    mood = entry["mood"] as String,
                    sleepHours = entry["sleepHours"] as Double,
                    breakfast = entry["breakfast"] as List<String>,
                    lunch = entry["lunch"] as List<String>,
                    dinner = entry["dinner"] as List<String>,
                    lactose = entry["lactose"] as Boolean,
                    gluten = entry["gluten"] as Boolean
                )
            }
        } catch (e: Exception) {
            logger.error("Error loading entries: ${e.message}", e)
            emptyList()
        }

        // Old JSON implementation:
        // try {
        //     getCurrentEntries(File(filename))
        // } catch (e: Exception) {
        //     println("Error loading entries: ${e.message}")
        //     mutableListOf()
        // }
    }

    // Old JSON helper method:
    // private fun getCurrentEntries(file: File): MutableList<DailyEntry> {
    //     val existingEntries = if (file.exists() && file.readText().isNotBlank()) {
    //         Json.decodeFromString<List<DailyEntry>>(file.readText())
    //     } else {
    //         mutableListOf()
    //     }
    //     return existingEntries.toMutableList()
    // }
}
