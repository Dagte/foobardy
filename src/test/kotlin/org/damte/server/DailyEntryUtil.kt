package org.damte.server

import kotlinx.datetime.LocalDate
import org.damte.org.damte.server.model.DailyEntry

private const val SAMPLE_DATE = "2024-11-25"
private const val SAMPLE_MOOD = "Neutral"
private const val SAMPLE_SLEEP_TIME = 7.0

object DailyEntryUtil {

    fun getOne(date: LocalDate, breakfast: List<String>, lunch: List<String> , dinner: List<String> ) = getOne(
        date,
        breakfast,
        lunch,
        dinner,
        SAMPLE_MOOD,
        SAMPLE_SLEEP_TIME,
    )

    fun getOne(breakfast: List<String>, lunch: List<String> , dinner: List<String> ) = getOne(
        LocalDate.parse(SAMPLE_DATE),
        breakfast,
        lunch,
        dinner,
        SAMPLE_MOOD,
        SAMPLE_SLEEP_TIME,
    )

    private fun getOne(date: LocalDate, breakfast: List<String>, lunch: List<String> , dinner: List<String>, mood: String, sleepHours: Double) = DailyEntry(
        date = date,
        breakfast = breakfast,
        lunch = lunch,
        dinner = dinner,
        mood = mood,
        sleepHours = sleepHours,
        lactose = false,
        gluten = false
    )
}