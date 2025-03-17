package org.damte.org.damte.server.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.damte.org.damte.server.model.DailyEntry
import org.damte.org.damte.server.model.request.DailyEntryCreateRequest
import org.damte.org.damte.server.model.request.DailyEntryUpdateRequest
import org.damte.server.model.dto.UpdateDailyEntry

/**
 * Extension function to convert DailyEntryRequest to DailyEntry.
 */
fun DailyEntryCreateRequest.toDailyEntry(): DailyEntry {
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

/**
 * Extension function to convert DailyEntryRequest to DailyEntry.
 */
fun DailyEntryUpdateRequest.toUpdateDailyEntry(): UpdateDailyEntry {
    return UpdateDailyEntry(
        date = this.date,
        mood = this.mood,
        sleepHours = this.sleepHours,
        breakfast = this.breakfast,
        lunch = this.lunch,
        dinner = this.dinner,
        gluten = this.gluten,
        lactose = this.lactose,
    )
}