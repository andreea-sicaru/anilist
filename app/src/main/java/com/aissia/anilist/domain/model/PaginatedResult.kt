package com.aissia.anilist.domain.model

data class PaginatedResult<T>(
    val items: List<T>,
    val hasNextPage: Boolean,
)
