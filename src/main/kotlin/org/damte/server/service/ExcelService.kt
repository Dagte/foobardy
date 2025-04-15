package org.damte.server.service

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.damte.server.database.DailyEntryRepository
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import kotlinx.datetime.*

class ExcelService {
    private val logger = LoggerFactory.getLogger(ExcelService::class.java)

    fun generateDailyEntriesExcel(): ByteArray {
        logger.info("Generating Excel file for daily entries")
        
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Daily Entries")
        
        val headers = createHeaderRow(sheet)
        
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentMonth = now.month
        val currentYear = now.year
        
        logger.info("Filtering entries for ${currentMonth.name} $currentYear")
        
        val entries = DailyEntryRepository.getAllEntries().filter { entry ->
            val entryDate = LocalDate.parse(entry["date"] as String)
            entryDate.month == currentMonth && entryDate.year == currentYear
        }
        
        entries.forEachIndexed { index, entry ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(entry["date"] as String)
            row.createCell(1).setCellValue(entry["mood"] as String)
            row.createCell(2).setCellValue(entry["sleepHours"] as Double)
            row.createCell(3).setCellValue((entry["breakfast"] as List<String>).joinToString(", "))
            row.createCell(4).setCellValue((entry["lunch"] as List<String>).joinToString(", "))
            row.createCell(5).setCellValue((entry["dinner"] as List<String>).joinToString(", "))
            row.createCell(6).setCellValue(entry["lactose"] as Boolean)
            row.createCell(7).setCellValue(entry["gluten"] as Boolean)
        }

        autosizeColumns(headers, sheet)

        val outputStream = writeToByteArray(workbook)

        logger.info("Excel file generated successfully with ${entries.size} entries for ${currentMonth.name} $currentYear")
        return outputStream.toByteArray()
    }

    private fun writeToByteArray(workbook: XSSFWorkbook): ByteArrayOutputStream {
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return outputStream
    }

    private fun autosizeColumns(headers: List<String>, sheet: XSSFSheet) {
        for (i in 0 until headers.size) {
            sheet.autoSizeColumn(i)
        }
    }

    private fun createHeaderRow(sheet: XSSFSheet): List<String> {
        val headerRow = sheet.createRow(0)
        val headers = listOf("Date", "Mood", "Sleep Hours", "Breakfast", "Lunch", "Dinner", "Lactose", "Gluten")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }
        return headers
    }
} 