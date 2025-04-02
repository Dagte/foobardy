package org.damte.server.model.request

import kotlinx.datetime.LocalDate

data class EntryQueryParams(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val page: Int = 1,
    val pageSize: Int = 20,
    val sortBy: String = "date",
    val sortOrder: String = "desc"
)
