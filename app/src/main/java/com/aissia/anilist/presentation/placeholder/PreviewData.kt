package com.aissia.anilist.presentation.placeholder

import com.aissia.anilist.domain.model.AnimePreview

object PreviewData {
    val animePreview = AnimePreview(
        id = 1,
        title = "Superman: Here he goes again",
        coverImageLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/250-JpXhinXPqpNE.jpg",
        coverImageExtraLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/250-JpXhinXPqpNE.jpg",
        coverImageColor = "#e4ae50",
        averageScore = 91.0,
        genres = listOf("SF", "fantasy", "horror"),
        duration = 75,
    )
    val animePreviewList = listOf(
        animePreview,
        animePreview.copy(id = 2),
        animePreview.copy(id = 3),
    )

}