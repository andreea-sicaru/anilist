package com.aissia.anilist.domain.repository

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.domain.model.PaginatedResult

// ViewModels depend on this interface directly. If business logic grows beyond simple data
// fetching (e.g. caching rules, input validation, cross-repository orchestration), introducing
// a use case layer between ViewModels and this repository is the natural next step.
interface AnimeRepository {
    suspend fun getHomeSections(): Result<HomeSections>
    suspend fun getPopularAnime(page: Int, perPage: Int): Result<PaginatedResult<AnimePreview>>
    suspend fun getNowShowingAnime(page: Int, perPage: Int): Result<PaginatedResult<AnimePreview>>
    suspend fun getAnimeDetail(id: Int): Result<Anime>
}
