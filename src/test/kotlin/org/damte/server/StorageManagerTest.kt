package org.damte.server

import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.StorageManager.addEntries
import org.damte.org.damte.server.model.DailyEntry
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.damte.org.damte.server.StorageManager.saveEntries
import org.damte.org.damte.server.StorageManager.updateEntry

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertContains

class StorageManagerTest {
    @TempDir
    lateinit var tempDir: File

    private lateinit var testFile: File
    private val json = Json { prettyPrint = false }

    @BeforeEach
    fun setup() {
        testFile = File(tempDir, "test_entries.json")
    }

    @Test
    fun `saveEntries creates a file with entries`() {
        val entries = listOf(
            DailyEntry(
                date = LocalDate(2024, 11, 26),
                mood = "Happy",
                sleepHours = 8.0,
                breakfast = "Oatmeal with fruits",
                lunch = "Grilled chicken sandwich",
                dinner = "Spaghetti Bolognese"
            )
        )

        addEntries(entries, testFile.absolutePath)

        assertTrue(testFile.exists(), "File should be created")

        val expectedJson = json.encodeToString<List<DailyEntry>>(entries)
        assertEquals(expectedJson, testFile.readText(), "JSON content should match")
    }


    @Test
    fun `saveEntries updates the file with new entries`() {
        val initialEntries = listOf(
            DailyEntry(
                date = LocalDate.parse("2024-11-25"),
                mood = "Neutral",
                sleepHours = 7.0,
                breakfast = "Toast",
                lunch = "Salad",
                dinner = "Soup"
            )
        )
        addEntries(initialEntries)

        val newEntries = listOf(
            DailyEntry(
                date = LocalDate.parse("2024-11-25"),
                mood = "Neutral",
                sleepHours = 7.0,
                breakfast = "Toast",
                lunch = "Salad",
                dinner = "Soup"
            )
        )

        addEntries(newEntries, testFile.absolutePath)

        assertTrue(testFile.exists(), "File should be created")

        val first = json.encodeToString<List<DailyEntry>>(initialEntries)
        val second = json.encodeToString<List<DailyEntry>>(newEntries)
        assertContains(testFile.readText(), first)
        assertContains(testFile.readText(), second)
    }

    @Test
    fun `saveEntries with empty list`() {
        val emptyEntries = emptyList<DailyEntry>()
       saveEntries(emptyEntries)

        addEntries(emptyEntries, testFile.absolutePath)

        assertTrue(testFile.exists(), "File should be created")

        val expectedJson = json.encodeToString<List<DailyEntry>>(emptyEntries)
        assertEquals(expectedJson, testFile.readText(), "JSON content should match")
    }

//    @Test
//    fun `updateEntry when second entry has same date`() {
//        val entries = listOf(
//            DailyEntry(
//                date = LocalDate(2024, 11, 26),
//                mood = "Ecstatic",
//                sleepHours = 4.0,
//                breakfast = "Omelette",
//                lunch = "Chicken a la caserole",
//                dinner = "Tuna"
//            )
//        )
//        addEntries(entries, testFile.absolutePath)
//
//        updateEntry(entries, testFile.absolutePath)
//
//        assertTrue(testFile.exists(), "File should be created")
//
//        val expectedJson = json.encodeToString<List<DailyEntry>>(entries)
//        assertEquals(expectedJson, testFile.readText(), "JSON content should match")
//    }

    @Test
    fun `updateEntry updates existing entry`() {
        val entry = DailyEntry(
            date = LocalDate(2024, 11, 26),
            mood = "Ecstatic",
            sleepHours = 4.0,
            breakfast = "Omelette",
            lunch = "Chicken a la caserole",
            dinner = "Tuna"
        )
        addEntries(listOf(entry), testFile.absolutePath)

        updateEntry(entry.copy(dinner = "Gazpacho"), testFile.absolutePath)

        val entries = json.decodeFromString<List<DailyEntry>>(testFile.readText())
        assertEquals(1, entries.size)
        assertEquals("Gazpacho", entries[0].dinner)
    }

    @Test
    fun `updateEntry adds new entry if date not found`() {
        val today = LocalDate(2024, 11, 26)
        val entry = DailyEntry(
            date = today,
            mood = "Excited",
            sleepHours = 6.5,
            breakfast = "Pancakes",
            lunch = "Burger",
            dinner = "Pizza"
        )

        addEntries(listOf(entry), testFile.absolutePath)
        val yesterday = LocalDate(2024, 11, 25)
        val newEntry = entry.copy(date = yesterday)

        updateEntry (newEntry, testFile.absolutePath)

        val entries = json.decodeFromString<List<DailyEntry>>(testFile.readText())
        assertEquals(2, entries.size)
        assertEquals(today, entries[0].date)
        assertEquals(yesterday, entries[1].date)
    }
}