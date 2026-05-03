package com.aissia.anilist.domain.usecase

import com.aissia.anilist.domain.repository.AnimeRepository
import javax.inject.Inject

private const val DEFAULT_PER_PAGE = 20

class GetNowShowingAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(page: Int, perPage: Int = DEFAULT_PER_PAGE) =
        repository.getNowShowingAnime(page, perPage)
}
