package org.damte

import org.damte.model.DailyEntry
import storage.StorageManager
import ui.ConsoleUI

fun main() {
// Load existing entries from storage
    val entries: MutableList<DailyEntry> = StorageManager.loadEntries()

    // Main application loop
    loop@ while (true) {
        when (ConsoleUI.displayMenu()) {
            "1" -> {
                val entry = ConsoleUI.collectDailyData()

                // Check if an entry for today already exists
                if (entries.any { it.date == entry.date }) {
                    println("\nAn entry for today already exists. Do you want to overwrite it? (y/n)")
                    when (readLine()?.lowercase()) {
                        "y", "yes" -> {
                            entries.removeAll { it.date == entry.date }
                            entries.add(entry)
                            StorageManager.saveEntries(entries)
                            println("Today's data has been updated.")
                        }
                        else -> println("No changes made.")
                    }
                } else {
                    entries.add(entry)
                    StorageManager.saveEntries(entries)
                    println("Data saved successfully.")
                }
            }
            "2" -> {
                ConsoleUI.displayEntries(entries)
            }
            "3" -> {
                println("\nThank you for using the Calendar Progress Tracker. Goodbye!")
                break@loop
            }
            else -> println("\nInvalid option. Please select a valid choice.")
        }
    }}