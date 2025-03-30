package org.damte.server.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.date

object DailyEntries : Table() {
    val id = integer("id").autoIncrement()
    val date = date("date")  // This now uses kotlinx.datetime.LocalDate
    val mood = varchar("mood", 255)
    val sleepHours = double("sleep_hours")
    val lactose = bool("lactose")
    val gluten = bool("gluten")

    override val primaryKey = PrimaryKey(id)
}

object Meals : Table() {
    val id = integer("id").autoIncrement()
    val entryId = integer("entry_id").references(DailyEntries.id)
    val mealType = varchar("meal_type", 50) // breakfast, lunch, dinner
    val foodItem = varchar("food_item", 255)

    override val primaryKey = PrimaryKey(id)
} 