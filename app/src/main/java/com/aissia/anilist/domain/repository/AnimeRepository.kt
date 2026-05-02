package com.aissia.anilist.domain.repository

import com.aissia.anilist.domain.model.Anime

interface AnimeRepository {
    suspend fun getPopularAnime(page: Int, perPage: Int = 50): Result<Pair<List<Anime>, Boolean>>

    suspend fun getNowShowingAnime(page: Int, perPage: Int = 50): Result<Pair<List<Anime>, Boolean>>
    suspend fun getAnimeDetail(id: Int): Result<Anime>
}
