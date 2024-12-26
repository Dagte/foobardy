package org.damte.org.damte.server.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.damte.org.damte.server.model.DailyEntry
import org.damte.org.damte.server.model.request.DailyEntryRequest

/**
 * Extension function to convert DailyEntryRequest to DailyEntry.
 */
fun DailyEntryRequest.toDailyEntry(): DailyEntry {
    val tz = TimeZone.currentSystemDefault()
    return DailyEntry(
        date = Clock.System.todayIn(tz),
        mood = this.mood,
        sleepHours = this.sleepHours,
        breakfast = this.breakfast,
        lunch = this.lunch,
        dinner = this.dinner,
        gluten = this.gluten ?: false,
        lactose = this.lactose ?: false,
    )
}