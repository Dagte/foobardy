package org.damte.server

import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.damte.org.damte.server.StorageManager
import org.damte.org.damte.server.model.DailyEntry
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class StorageManagerTest {
    @TempDir
    lateinit var tempDir: File

    private lateinit var testFile: File
    private lateinit var storageManager: StorageManager
    private val json = Json { prettyPrint = false }

    @BeforeEach
    fun setup() {
        // Initialize the temporary test file
        testFile = File(tempDir, "test_entries.json")
        storageManager = StorageManager(testFile.absolutePath)
    }

    @Test
    fun `saveEntries creates a file with entries`() {
        val entries = listOf(
            DailyEntryUtil.getOne(LocalDate(2024, 11, 26), listOf("Oatmeal", "Berries"), listOf("Sandwich", "Chicken"), listOf("Spaghetti")))

        storageManager.addEntries(entries,)

        assertTrue(testFile.exists(), "File should be created")

        val expectedJson = json.encodeToString<List<DailyEntry>>(entries)
        assertEquals(expectedJson, testFile.readText(), "JSON content should match")
    }


    @Test
    fun `addEntries just add second entry if duplicated entries`() {
        val initialEntries = listOf(
            DailyEntryUtil.getOne(listOf("Toast"), listOf("Salad"), listOf("Soup"))
        )
        storageManager.addEntries(initialEntries)

        val duplicatedEntry =  DailyEntryUtil.getOne(listOf("Toast"), listOf("Salad"), listOf("Soup"));
        val newEntries = listOf(duplicatedEntry)

        storageManager.addEntries(newEntries)

        assertTrue(testFile.exists(), "File should be created")

        val entries = Json.decodeFromString<List<DailyEntry>>(testFile.readText())
        assertEquals(entries.size, 2)
        assertEquals(entries[0], entries[1])
    }

    @Test
    fun `saveEntries with empty list`() {
        val emptyEntries = emptyList<DailyEntry>()
        storageManager.saveEntries(emptyEntries)

        storageManager.addEntries(emptyEntries)

        assertTrue(testFile.exists(), "File should be created")

        val expectedJson = json.encodeToString<List<DailyEntry>>(emptyEntries)
        assertEquals(expectedJson, testFile.readText(), "JSON content should match")
    }

    @Test
    fun `updateEntry updates existing entry`() {
        val entry = DailyEntryUtil.getOne(
            LocalDate(2024, 11, 26),
            listOf("Omelette"),
            listOf("Chicken a la caserole"),
            listOf("Tuna")
        )
        storageManager.addEntries(listOf(entry),)

        storageManager.updateEntry(entry.copy(dinner = listOf("Gazpacho")),)

        val entries = json.decodeFromString<List<DailyEntry>>(testFile.readText())
        assertEquals(1, entries.size)
        assertEquals(listOf("Gazpacho"), entries[0].dinner)
    }

    @Test
    fun `updateEntry adds new entry if date not found`() {
        val today = LocalDate(2024, 11, 26)
        val entry =
            DailyEntryUtil.getOne(
                LocalDate(2024, 11, 26),
                listOf("Pancakes"),
                listOf("Burger"),
                listOf("Pizza")
            )

        storageManager.addEntries(listOf(entry),)
        val yesterday = LocalDate(2024, 11, 25)
        val newEntry = entry.copy(date = yesterday)

        storageManager.updateEntry (newEntry,)

        val entries = json.decodeFromString<List<DailyEntry>>(testFile.readText())
        assertEquals(2, entries.size)
        assertEquals(today, entries[0].date)
        assertEquals(yesterday, entries[1].date)
    }
}