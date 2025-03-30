package org.damte.server.controller

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.damte.server.auth.validateAuthentication
import org.damte.server.database.DailyEntries
import org.damte.server.database.Meals
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.serialization.Serializable

@Serializable
data class DailyEntryResponse(
    val id: Int,
    val date: String,
    val mood: String,
    val sleepHours: Double
)

@Serializable
data class MealSummaryResponse(
    val type: String,
    val count: String
)

fun Route.adminRoutes() {
    route("/admin") {
        get("/db/stats") {
            validateAuthentication()

            val stats = transaction {
                mapOf(
                    "dailyEntries" to DailyEntries.selectAll().count().toString(),
                    "meals" to Meals.selectAll().count().toString(),
                    "knownTables" to DailyEntries.tableName + "," + Meals.tableName,
                    "databasePath" to "./data/dailyentries.db"
                )
            }
            call.respond(stats)
        }

        get("/db/entries/latest") {
            validateAuthentication()

            val latestEntries = transaction {
                DailyEntries
                    .selectAll()
                    .orderBy(DailyEntries.date to SortOrder.DESC)
                    .limit(5)
                    .map { row ->
                        DailyEntryResponse(
                            id = row[DailyEntries.id],
                            date = row[DailyEntries.date].toString(),
                            mood = row[DailyEntries.mood].toString(),
                            sleepHours = row[DailyEntries.sleepHours]
                        )
                    }
            }
            call.respond(latestEntries)
        }

        get("/db/meals/summary") {
            validateAuthentication()

            val mealSummary = transaction {
                Meals
                    .slice(Meals.mealType, Meals.foodItem.count())
                    .selectAll()
                    .groupBy(Meals.mealType)
                    .map { row ->
                        MealSummaryResponse(
                            type = row[Meals.mealType],
                            count = row[Meals.foodItem.count()].toString()
                        )
                    }
            }
            call.respond(mealSummary)
        }
    }
} 