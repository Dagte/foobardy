package org.damte.org.damte.server.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DailyEntryRequest(
    val mood: String,
    val sleepHours: Double,
    val breakfast: List<String>,
    val lunch: List<String>,
    val dinner: List<String>,
    val lactose: Boolean? = null,
    val gluten: Boolean? = null
)