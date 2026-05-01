package com.aissia.anilist.domain.usecase

import com.aissia.anilist.domain.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: Int) = repository.getAnimeDetail(id)
}
