package org.damte.server.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PaginationMetadata(
    val currentPage: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int
) 