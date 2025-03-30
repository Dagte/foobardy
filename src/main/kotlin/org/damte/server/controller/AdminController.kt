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

fun Route.adminRoutes() {
    route("/admin") {
        get("/db/stats") {
            validateAuthentication()

            val stats = transaction {
                mapOf(
                    "dailyEntries" to DailyEntries.selectAll().count(),
                    "meals" to Meals.selectAll().count(),
                    "knownTables" to listOf(DailyEntries.tableName, Meals.tableName),
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
                        mapOf(
                            "id" to row[DailyEntries.id],
                            "date" to row[DailyEntries.date],
                            "mood" to row[DailyEntries.mood],
                            "sleepHours" to row[DailyEntries.sleepHours]
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
                        mapOf(
                            "type" to row[Meals.mealType],
                            "count" to row[Meals.foodItem.count()]
                        )
                    }
            }
            call.respond(mealSummary)
        }
    }
} 