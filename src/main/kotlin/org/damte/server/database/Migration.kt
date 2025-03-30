package org.damte.server.database

import kotlinx.serialization.json.*
import kotlinx.datetime.LocalDate

object Migration {
    fun migrateFromJson(jsonContent: String) {
        val jsonArray = Json.parseToJsonElement(jsonContent).jsonArray

        jsonArray.forEach { entry ->
            val obj = entry.jsonObject
            DailyEntryRepository.insertEntry(
                date = LocalDate.parse(obj["date"]?.jsonPrimitive?.content ?: "1970-01-01"),
                mood = obj["mood"]?.jsonPrimitive?.content ?: "",
                sleepHours = obj["sleepHours"]?.jsonPrimitive?.double ?: 0.0,
                breakfast = obj["breakfast"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                lunch = obj["lunch"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                dinner = obj["dinner"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                lactose = obj["lactose"]?.jsonPrimitive?.boolean ?: false,
                gluten = obj["gluten"]?.jsonPrimitive?.boolean ?: false
            )
        }
    }
} 