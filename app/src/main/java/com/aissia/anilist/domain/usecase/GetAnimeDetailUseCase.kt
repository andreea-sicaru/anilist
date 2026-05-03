package com.aissia.anilist.domain.usecase

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: Int): Result<Anime> {
        require(id > 0) { "Anime id must be positive, got $id" }
        return repository.getAnimeDetail(id)
    }
}
