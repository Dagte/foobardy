package org.damte.org.damte.server.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DailyEntryRequest(
    val mood: String,
    val sleepHours: Double,
    val breakfast: String,
    val lunch: String,
    val dinner: String
)