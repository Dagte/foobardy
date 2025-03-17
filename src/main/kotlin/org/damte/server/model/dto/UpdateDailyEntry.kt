package org.damte.server.model.dto

import kotlinx.datetime.LocalDate

data class UpdateDailyEntry (
    val date: LocalDate,
    val mood: String? = null,
    val sleepHours: Double? = null,
    val breakfast: List<String>? = listOf(),
    val lunch: List<String>? = listOf(),
    val dinner: List<String>? = listOf(),
    val lactose: Boolean? = null,
    val gluten: Boolean? = null
)