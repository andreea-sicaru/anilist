package com.aissia.anilist.domain.repository

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.domain.model.PaginatedResult

interface AnimeRepository {
    suspend fun getHomeData(): Result<HomeSections>
    suspend fun getPopularAnime(page: Int, perPage: Int): Result<PaginatedResult<AnimePreview>>
    suspend fun getNowShowingAnime(page: Int, perPage: Int): Result<PaginatedResult<AnimePreview>>
    suspend fun getAnimeDetail(id: Int): Result<Anime>
}
