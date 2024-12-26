package org.damte.org.damte.server.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class DailyEntry(
    val date: LocalDate,
    val mood: String,
    val sleepHours: Double,
    val breakfast: List<String>,
    val lunch: List<String>,
    val dinner: List<String>,
    val lactose: Boolean,
    val gluten: Boolean
)