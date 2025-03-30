package org.damte.server.database

import kotlinx.serialization.json.*
import kotlinx.datetime.LocalDate
import org.slf4j.LoggerFactory
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import java.io.File

object Migration {
    private val logger = LoggerFactory.getLogger(Migration::class.java)

    fun migrateFromJson(jsonContent: String) {
        logger.info("Starting migration from JSON file")
        try {
        
            val existingCount = transaction {
                DailyEntries.selectAll().count()
            }
            
            if (existingCount > 0) {
                logger.info("Database already contains $existingCount entries. Skipping migration.")
                return
            }

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
            
            // After successful migration, rename or delete the JSON file to prevent future migrations
            val entriesFile = File("entries.json")
            val backupFile = File("entries.json.migrated")
            if (backupFile.exists()) {
                backupFile.delete()
            }
            entriesFile.renameTo(backupFile)
            logger.info("Migration completed successfully and source file renamed to prevent re-migration")
            
        } catch (e: Exception) {
            logger.error("Migration failed", e)
            throw e
        }
    }
} 