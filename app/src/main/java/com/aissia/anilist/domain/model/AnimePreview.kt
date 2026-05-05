package com.aissia.anilist.domain.model

data class AnimePreview(
    val id: Int,
    val title: String,
    val coverImageLarge: String?,
    val coverImageExtraLarge: String?,
    val coverImageColor: String?,
    val averageScore: Double?,
    val genres: List<String>,
    val duration: Int?,
)
