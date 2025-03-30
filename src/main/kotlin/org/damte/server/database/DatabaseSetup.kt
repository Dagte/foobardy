package org.damte.server.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import org.slf4j.LoggerFactory

class DatabaseSetup {
    private val logger = LoggerFactory.getLogger(DatabaseSetup::class.java)

    fun initialize() {
        logger.info("Initializing database connection")
        setupConnection()
        createTables()
        logger.info("Database initialization completed")
    }

    private fun setupConnection() {
        logger.debug("Setting up database connection")
        val driverClassName = "org.sqlite.JDBC"
        val dataDir = File("./data")
        
        if (!dataDir.exists()) {
            dataDir.mkdirs()
            logger.info("Created data directory: ${dataDir.absolutePath}")
        }
        
        val jdbcURL = "jdbc:sqlite:./data/dailyentries.db"
        org.jetbrains.exposed.sql.Database.connect(jdbcURL, driverClassName)
        logger.info("Database connection established at: $jdbcURL")
    }

    private fun createTables() {
        logger.debug("Creating database tables")
        transaction {
            SchemaUtils.create(DailyEntries, Meals)
            logger.info("Database tables created successfully")
        }
    }

    fun migrateExistingData() {
        logger.debug("Checking for existing data to migrate")
        
        // Check if database already has data
        val hasData = transaction {
            DailyEntries.selectAll().count() > 0
        }
        
        if (hasData) {
            logger.info("Database already contains data, skipping migration")
            return
        }

        val entriesFile = File("entries.json")
        if (entriesFile.exists()) {
            logger.info("Found existing data file: ${entriesFile.absolutePath}")
            val jsonContent = entriesFile.readText()
            Migration.migrateFromJson(jsonContent)
            
            // Rename the file after successful migration
            val migratedFile = File("entries.json.migrated")
            if (migratedFile.exists()) {
                migratedFile.delete()
            }
            if (entriesFile.renameTo(migratedFile)) {
                logger.info("Successfully renamed entries.json to entries.json.migrated")
            } else {
                logger.warn("Failed to rename entries.json file")
            }
            
            logger.info("Data migration completed")
        } else {
            logger.info("No existing data file found for migration")
        }
    }
}