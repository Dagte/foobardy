package org.damte.server.database

import kotlinx.datetime.LocalDate
import org.damte.org.damte.server.model.DailyEntry
import org.slf4j.LoggerFactory

object DataSeeder {
    private val logger = LoggerFactory.getLogger(DataSeeder::class.java)

    fun seedApril2025Data() {
        val entries = listOf(
            DailyEntry(
                date = LocalDate(2025, 4, 1),
                mood = "Good",
                sleepHours = 7.5,
                breakfast = listOf("Oatmeal", "Banana", "Coffee"),
                lunch = listOf("Chicken Salad", "Bread"),
                dinner = listOf("Fish", "Rice", "Vegetables"),
                lactose = false,
                gluten = false
            ),
            DailyEntry(
                date = LocalDate(2025, 4, 2),
                mood = "Great",
                sleepHours = 8.0,
                breakfast = listOf("Yogurt", "Granola", "Fruit"),
                lunch = listOf("Soup", "Sandwich"),
                dinner = listOf("Pasta", "Tomato Sauce", "Cheese"),
                lactose = true,
                gluten = true
            ),
            DailyEntry(
                date = LocalDate(2025, 4, 3),
                mood = "Okay",
                sleepHours = 6.5,
                breakfast = listOf("Toast", "Eggs", "Tea"),
                lunch = listOf("Rice Bowl", "Vegetables"),
                dinner = listOf("Pizza", "Salad"),
                lactose = true,
                gluten = true
            ),
            DailyEntry(
                date = LocalDate(2025, 4, 4),
                mood = "Good",
                sleepHours = 7.0,
                breakfast = listOf("Smoothie", "Toast"),
                lunch = listOf("Quinoa Bowl", "Chicken"),
                dinner = listOf("Stir Fry", "Rice"),
                lactose = false,
                gluten = false
            ),
            DailyEntry(
                date = LocalDate(2025, 4, 5),
                mood = "Excellent",
                sleepHours = 8.5,
                breakfast = listOf("Pancakes", "Maple Syrup", "Coffee"),
                lunch = listOf("Burger", "Fries"),
                dinner = listOf("Sushi", "Miso Soup"),
                lactose = false,
                gluten = true
            ),
            DailyEntry(
                date = LocalDate(2025, 4, 6),
                mood = "Good",
                sleepHours = 7.5,
                breakfast = listOf("Cereal", "Milk", "Fruit"),
                lunch = listOf("Wrap", "Chips"),
                dinner = listOf("Grilled Chicken", "Sweet Potato", "Salad"),
                lactose = true,
                gluten = true
            ),
            DailyEntry(
                date = LocalDate(2025, 4, 7),
                mood = "Great",
                sleepHours = 8.0,
                breakfast = listOf("Bagel", "Cream Cheese", "Coffee"),
                lunch = listOf("Tacos", "Guacamole"),
                dinner = listOf("Steak", "Mashed Potatoes", "Vegetables"),
                lactose = true,
                gluten = true
            )
        )

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
            logger.info("Successfully added April 2025 data")
        } catch (e: Exception) {
            logger.error("Error adding April 2025 data: ${e.message}", e)
            throw e
        }
    }
} 