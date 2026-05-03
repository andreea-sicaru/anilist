package com.aissia.anilist.presentation.navigation

import com.aissia.anilist.presentation.animelist.AnimeListType
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailRoute(val animeId: Int)

@Serializable
data class AnimeListRoute(val listType: AnimeListType)
