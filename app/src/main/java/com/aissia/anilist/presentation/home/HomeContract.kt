package com.aissia.anilist.presentation.home

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.presentation.UiState

object HomeContract {

    data class State(
        val nowShowing: UiState<List<Anime>> = UiState.Loading,
        val popular: UiState<List<Anime>> = UiState.Loading,
        val currentPopularPage: Int = 1,
        val hasMorePopular: Boolean = true,
        val isPaginatingPopular: Boolean = false,
    )

    sealed class Event {
        object LoadInitialData : Event()
        data class AnimeClicked(val animeId: Int) : Event()
        object RetryTrending : Event()
        object LoadMorePopular : Event()
        object RetryPopular : Event()
    }

    sealed class Effect {
        data class NavigateToDetail(val animeId: Int) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}
