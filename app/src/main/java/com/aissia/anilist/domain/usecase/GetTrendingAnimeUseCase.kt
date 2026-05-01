package com.aissia.anilist.domain.usecase

import com.aissia.anilist.domain.repository.AnimeRepository
import javax.inject.Inject

class GetTrendingAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke() = repository.getTrendingAnime()
}
