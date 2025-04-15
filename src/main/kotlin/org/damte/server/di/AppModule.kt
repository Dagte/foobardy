package org.damte.server.di

import org.damte.org.damte.server.StorageManager
import org.damte.server.database.DatabaseSetup
import org.damte.server.service.ExcelService
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import org.slf4j.Logger

private val logger: Logger = LoggerFactory.getLogger("AppModule")

val AppModule = module {
    // Log configuration at module initialization
    logger.info("Configuring application dependencies")
    
    single { 
        logger.info("Creating StorageManager instance")
        StorageManager() 
    }
    
    single { 
        logger.info("Creating ExcelService instance")
        ExcelService() 
    }
    
    single(createdAtStart = true) { 
        logger.info("Initializing DatabaseSetup")
        DatabaseSetup().apply {
            initialize()
            migrateExistingData()
        }.also {
            logger.info("DatabaseSetup initialization completed")
        }
    }
}
