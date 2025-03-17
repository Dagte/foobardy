package org.damte.org.damte.server.model.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DailyEntryUpdateRequest(
    val date: LocalDate,
    val mood: String? = null,
    val sleepHours: Double? = null,
    val breakfast: List<String>? = null,
    val lunch: List<String>? = null,
    val dinner: List<String>? = null,
    val lactose: Boolean? = null,
    val gluten: Boolean? = null
)