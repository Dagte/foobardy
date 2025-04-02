package org.damte.server.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.datetime.LocalDate

object DailyEntryRepository {
    fun insertEntry(
        date: LocalDate,
        mood: String,
        sleepHours: Double,
        breakfast: List<String>,
        lunch: List<String>,
        dinner: List<String>,
        lactose: Boolean,
        gluten: Boolean
    ) {
        transaction {
            val entryId = DailyEntries.insert {
                it[DailyEntries.date] = date
                it[DailyEntries.mood] = mood
                it[DailyEntries.sleepHours] = sleepHours
                it[DailyEntries.lactose] = lactose
                it[DailyEntries.gluten] = gluten
            } get DailyEntries.id

            // Insert meals
            breakfast.forEach { food ->
                Meals.insert {
                    it[Meals.entryId] = entryId
                    it[Meals.mealType] = "breakfast"
                    it[Meals.foodItem] = food
                }
            }

            lunch.forEach { food ->
                Meals.insert {
                    it[Meals.entryId] = entryId
                    it[Meals.mealType] = "lunch"
                    it[Meals.foodItem] = food
                }
            }

            dinner.forEach { food ->
                Meals.insert {
                    it[Meals.entryId] = entryId
                    it[Meals.mealType] = "dinner"
                    it[Meals.foodItem] = food
                }
            }
        }
    }

    fun getAllEntries(): List<Map<String, Any>> {
        return transaction {
            DailyEntries.selectAll().map { entry ->
                val meals = Meals.select { Meals.entryId eq entry[DailyEntries.id] }
                    .map { it[Meals.mealType] to it[Meals.foodItem] }
                    .groupBy { it.first }
                    .mapValues { it.value.map { it.second } }

                mapOf(
                    "date" to entry[DailyEntries.date].toString(),
                    "mood" to entry[DailyEntries.mood],
                    "sleepHours" to entry[DailyEntries.sleepHours],
                    "breakfast" to (meals["breakfast"] ?: emptyList()),
                    "lunch" to (meals["lunch"] ?: emptyList()),
                    "dinner" to (meals["dinner"] ?: emptyList()),
                    "lactose" to entry[DailyEntries.lactose],
                    "gluten" to entry[DailyEntries.gluten]
                )
            }
        }
    }

    fun getEntries(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1,
        pageSize: Int = 20,
        sortBy: String = "date",
        sortOrder: String = "desc"
    ): Pair<List<Map<String, Any>>, Long> {
        return transaction {
            // Start with base query
            var query = DailyEntries.selectAll()

            // Apply date filters
            if (startDate != null) {
                query = query.andWhere { DailyEntries.date greaterEq startDate }
            }
            if (endDate != null) {
                query = query.andWhere { DailyEntries.date lessEq endDate }
            }

            // Get total count before pagination
            val totalCount = query.count()

            // Apply sorting
            val order = if (sortOrder == "desc") SortOrder.DESC else SortOrder.ASC
            query = when (sortBy) {
                "date" -> query.orderBy(DailyEntries.date to order)
                "mood" -> query.orderBy(DailyEntries.mood to order)
                "sleepHours" -> query.orderBy(DailyEntries.sleepHours to order)
                else -> query.orderBy(DailyEntries.date to order)
            }

            // Apply pagination
            query = query.limit(pageSize, offset = ((page - 1) * pageSize).toLong())

            // Map results
            val entries = query.map { entry ->
                val meals = Meals.select { Meals.entryId eq entry[DailyEntries.id] }
                    .map { it[Meals.mealType] to it[Meals.foodItem] }
                    .groupBy { it.first }
                    .mapValues { it.value.map { it.second } }

                mapOf(
                    "date" to entry[DailyEntries.date].toString(),
                    "mood" to entry[DailyEntries.mood],
                    "sleepHours" to entry[DailyEntries.sleepHours],
                    "breakfast" to (meals["breakfast"] ?: emptyList()),
                    "lunch" to (meals["lunch"] ?: emptyList()),
                    "dinner" to (meals["dinner"] ?: emptyList()),
                    "lactose" to entry[DailyEntries.lactose],
                    "gluten" to entry[DailyEntries.gluten]
                )
            }

            Pair(entries, totalCount)
        }
    }
} 