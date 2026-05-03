package com.aissia.anilist.domain.model

data class Character(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val role: String?,
)
