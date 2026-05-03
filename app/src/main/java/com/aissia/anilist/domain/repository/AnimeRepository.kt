package com.aissia.anilist.domain.repository

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.PaginatedResult

interface AnimeRepository {
    suspend fun getPopularAnime(page: Int, perPage: Int): Result<PaginatedResult<Anime>>
    suspend fun getNowShowingAnime(page: Int, perPage: Int): Result<PaginatedResult<Anime>>
    suspend fun getAnimeDetail(id: Int): Result<Anime>
}
