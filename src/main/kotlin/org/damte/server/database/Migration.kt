package org.damte.server.database

import kotlinx.serialization.json.*
import kotlinx.datetime.LocalDate
import org.slf4j.LoggerFactory

object Migration {
    private val logger = LoggerFactory.getLogger(Migration::class.java)

    fun migrateFromJson(jsonContent: String) {
        logger.info("Starting migration from JSON file")
        try {
            val jsonArray = Json.parseToJsonElement(jsonContent).jsonArray
            logger.info("Found ${jsonArray.size} entries to migrate")

            jsonArray.forEachIndexed { index, entry ->
                try {
                    val obj = entry.jsonObject
                    val date = obj["date"]?.jsonPrimitive?.content ?: "1970-01-01"
                    
                    DailyEntryRepository.insertEntry(
                        date = LocalDate.parse(date),
                        mood = obj["mood"]?.jsonPrimitive?.content ?: "",
                        sleepHours = obj["sleepHours"]?.jsonPrimitive?.double ?: 0.0,
                        breakfast = obj["breakfast"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                        lunch = obj["lunch"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                        dinner = obj["dinner"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                        lactose = obj["lactose"]?.jsonPrimitive?.boolean ?: false,
                        gluten = obj["gluten"]?.jsonPrimitive?.boolean ?: false
                    )
                    logger.info("Successfully migrated entry $index with date $date")
                } catch (e: Exception) {
                    logger.error("Failed to migrate entry $index", e)
                }
            }
            logger.info("Migration completed successfully")
        } catch (e: Exception) {
            logger.error("Migration failed", e)
            throw e
        }
    }
} 