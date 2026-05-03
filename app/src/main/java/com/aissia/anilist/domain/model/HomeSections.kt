package com.aissia.anilist.domain.model

data class HomeSections(
    val nowShowing: List<AnimePreview>,
    val popular: List<AnimePreview>,
)
