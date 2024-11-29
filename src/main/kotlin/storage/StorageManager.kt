package storage

import org.damte.model.DailyEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object StorageManager {
    private const val FILENAME = "entries.json"

    fun saveEntries(entries: List<DailyEntry>) {
        try {
            val jsonString = Json.encodeToString(entries)
            File(FILENAME).writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving entries: ${e.message}")
        }
    }

    // Load entries from JSON file
    fun loadEntries(): MutableList<DailyEntry> {
        return try {
            val file = File(FILENAME)
            if (file.exists()) {
                val jsonString = file.readText()
                Json.decodeFromString(jsonString)
            } else {
                mutableListOf()
            }
        } catch (e: Exception) {
            println("Error loading entries: ${e.message}")
            mutableListOf()
        }
    }
}
