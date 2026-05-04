package com.aissia.anilist

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.domain.model.PaginatedResult
import com.aissia.anilist.domain.repository.AnimeRepository

class FakeAnimeRepository : AnimeRepository {

    var homeSectionsResult: Result<HomeSections> = Result.success(
        HomeSections(nowShowing = emptyList(), popular = emptyList())
    )
    var animeDetailResult: Result<Anime> = Result.success(fakeAnime)
    var popularResult: Result<PaginatedResult<AnimePreview>> = Result.success(
        PaginatedResult(items = emptyList(), hasNextPage = false)
    )
    var nowShowingResult: Result<PaginatedResult<AnimePreview>> = Result.success(
        PaginatedResult(items = emptyList(), hasNextPage = false)
    )

    override suspend fun getHomeSections() = homeSectionsResult
    override suspend fun getPopularAnime(page: Int, perPage: Int) = popularResult
    override suspend fun getNowShowingAnime(page: Int, perPage: Int) = nowShowingResult
    override suspend fun getAnimeDetail(id: Int) = animeDetailResult
}

val fakeAnimePreview = AnimePreview(
    id = 1,
    title = "Test Anime",
    coverImageLarge = null,
    coverImageExtraLarge = null,
    coverImageColor = null,
    averageScore = 8.5,
    genres = listOf("Action"),
    duration = 24,
)

val fakeAnime = Anime(
    id = 1,
    title = "Test Anime",
    coverImageLarge = null,
    coverImageExtraLarge = null,
    coverImageColor = null,
    bannerImage = null,
    averageScore = 8.5,
    popularity = 1000,
    genres = listOf("Action"),
    description = "A test anime.",
    trailer = null,
    status = null,
    seasonYear = 2024,
    duration = 24,
    isAdult = false,
    countryOfOrigin = "JP",
    characters = emptyList(),
)
