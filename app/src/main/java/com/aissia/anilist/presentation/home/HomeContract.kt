package com.aissia.anilist.presentation.home

import com.aissia.anilist.domain.model.Anime

object HomeContract {

    data class State(
        // Now Showing Section
        val isLoadingNowShowing: Boolean = true,
        val nowShowingAnime: List<Anime> = emptyList(),
        val nowShowingError: String? = null,

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

        // Now Showing Section
        object RetryTrending : Event()

        // Popular Section
        object LoadMorePopular: Event()
        object RetryPopular: Event()
    }

    sealed class Effect {
        data class ShowError(val message: String): Effect()
    }
}