package org.damte.server.model.response

import kotlinx.serialization.Serializable
import org.damte.org.damte.server.model.DailyEntry

@Serializable
data class EntriesResponse(
    val entries: List<DailyEntry>,
    val pagination: PaginationMetadata
) 