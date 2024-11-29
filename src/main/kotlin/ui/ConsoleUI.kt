package ui

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import kotlinx.datetime.todayIn
import org.damte.model.DailyEntry
import java.time.LocalDate

object ConsoleUI {
    // Display the main menu and return the user's choice
    fun displayMenu(): String? {
        println("\n--- Calendar Progress Tracker ---")
        println("Select an option:")
        println("1. Enter today's data")
        println("2. View all entries")
        println("3. Exit")
        print("Your choice: ")
        return readLine()
    }

    // Collect daily data from the user
    fun collectDailyData(): DailyEntry {
        val date = Clock.System.todayIn(TimeZone.currentSystemDefault())
        println("\n--- Enter Data for $date ---")

        val mood = getNonEmptyInput("Mood (e.g., Happy, Sad, Neutral): ")
        val sleepHours = getDoubleInput("Hours slept (e.g., 7.5): ")
        val breakfast = getNonEmptyInput("Breakfast: ")
        val lunch = getNonEmptyInput("Lunch: ")
        val dinner = getNonEmptyInput("Dinner: ")

        // Create and return a DailyEntry object
        return DailyEntry(
            date = date,
            mood = mood,
            sleepHours = sleepHours,
            breakfast = breakfast,
            lunch = lunch,
            dinner = dinner
        )
    }

    // Display all entries to the user
    fun displayEntries(entries: List<DailyEntry>) {
        if (entries.isEmpty()) {
            println("\nNo entries found.")
        } else {
            println("\n--- All Entries ---")
            entries.sortedBy { it.date }.forEach { entry ->
                println("""
                    Date: ${entry.date}
                    Mood: ${entry.mood}
                    Hours Slept: ${entry.sleepHours}
                    Breakfast: ${entry.breakfast}
                    Lunch: ${entry.lunch}
                    Dinner: ${entry.dinner}
                    ---------------------------
                """.trimIndent())
            }
        }
    }

    // Helper function to get non-empty input from the user
    private fun getNonEmptyInput(prompt: String): String {
        var input: String?
        do {
            print(prompt)
            input = readLine()
            if (input.isNullOrBlank()) {
                println("Input cannot be empty. Please try again.")
            }
        } while (input.isNullOrBlank())
        return input.trim()
    }

    // Helper function to get a valid double input from the user
    private fun getDoubleInput(prompt: String): Double {
        var input: String?
        var number: Double? = null
        do {
            print(prompt)
            input = readLine()
            number = input?.toDoubleOrNull()
            if (number == null || number < 0) {
                println("Please enter a valid non-negative number.")
            }
        } while (number == null || number < 0)
        return number
    }
}
