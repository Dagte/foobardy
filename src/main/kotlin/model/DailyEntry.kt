package org.damte.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class DailyEntry(
    val date: LocalDate,
    val mood: String,
    val sleepHours: Double,
    val breakfast: String,
    val lunch: String,
    val dinner: String
)