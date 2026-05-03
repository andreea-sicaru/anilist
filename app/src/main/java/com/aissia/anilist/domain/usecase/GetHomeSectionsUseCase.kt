package com.aissia.anilist.domain.usecase

import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.domain.repository.AnimeRepository
import javax.inject.Inject

class GetHomeSectionsUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(): Result<HomeSections> = repository.getHomeData()
}
