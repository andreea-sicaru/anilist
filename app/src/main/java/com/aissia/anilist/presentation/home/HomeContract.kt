package com.aissia.anilist.presentation.home

import com.aissia.anilist.domain.model.Anime

object HomeContract {

    data class State(

        // Popular Section
        val isLoadingPopular: Boolean = true,
        val popularAnime: List<Anime> = emptyList(),
        val popularError: String? = null,
        val currentPopularPage: Int = 1,
        val hasMorePopular: Boolean = true,
        val isPaginatingPopular: Boolean = false
    )

    sealed class Event {
        object LoadInitialData : Event()

        // Popular Section
        object LoadMorePopular: Event()
        object RetryPopular: Event()
    }

    sealed class Effect {
        data class ShowError(val message: String): Effect()
    }
}