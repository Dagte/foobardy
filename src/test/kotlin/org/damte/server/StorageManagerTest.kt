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

        StorageManager.addEntries(entries, testFile.absolutePath)

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

        assertTrue(testFile.exists(), "File should be created even with empty list")
        val expectedJson = json.encodeToString(emptyEntries)
        assertEquals(expectedJson, testFile.readText(), "JSON content should be empty array")
    }

//    @Test
//    fun `saveEntries handles invalid file path gracefully`() {
////        val invalidSaver = StoreManager("/invalid_path/test_entries.json")
//        val entries = listOf(
//            DailyEntry(
//                date = LocalDate.parse("2024-11-26"),
//                mood = "Happy",
//                sleepHours = 8.0,
//                breakfast = "Oatmeal with fruits",
//                lunch = "Grilled chicken sandwich",
//                dinner = "Spaghetti Bolognese"
//            )
//        )
//
//        // Capture the printed error message
//        val originalOut = System.out
//        val outputStream = java.io.ByteArrayOutputStream()
//        System.setOut(java.io.PrintStream(outputStream))
//
//        invalidSaver.saveEntries(entries)
//
//        System.setOut(originalOut)
//        val output = outputStream.toString().trim()
//        Assertions.assertTrue(output.startsWith("Error saving entries:"), "Should print error message")
//    }
}