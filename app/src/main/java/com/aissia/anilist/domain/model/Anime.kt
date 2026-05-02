package com.aissia.anilist.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val coverImageLarge: String?,
    val coverImageExtraLarge: String?,
    val coverImageColor: String?,
    val bannerImage: String?,
    val averageScore: Double?,
    val popularity: Int?,
    val genres: List<String>,
    val description: String?,
    val trailer: Trailer?,
    val status: String?,
    val seasonYear: Int?,
    val duration: Int? = null,
    val countryOfOrigin: String? = null
)
