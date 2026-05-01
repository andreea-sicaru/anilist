package com.aissia.anilist.domain.repository

import com.aissia.anilist.domain.model.Anime

interface AnimeRepository {
    suspend fun getTrendingAnime(page: Int = 1, perPage: Int = 10): Result<List<Anime>>
    suspend fun getPopularAnime(page: Int, perPage: Int = 20): Result<Pair<List<Anime>, Boolean>>
    suspend fun getAnimeDetail(id: Int): Result<Anime>
}
