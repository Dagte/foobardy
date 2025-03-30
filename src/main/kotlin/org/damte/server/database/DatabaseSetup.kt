package org.damte.server.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class DatabaseSetup {
    fun initialize() {
        setupConnection()
        createTables()
    }

    private fun setupConnection() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:./data/dailyentries.db"
        Database.connect(jdbcURL, driverClassName)
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(DailyEntries, Meals)
        }
    }

    fun migrateExistingData() {
        val entriesFile = File("entries.json")
        if (entriesFile.exists()) {
            val jsonContent = entriesFile.readText()
            Migration.migrateFromJson(jsonContent)
        }
    }
}