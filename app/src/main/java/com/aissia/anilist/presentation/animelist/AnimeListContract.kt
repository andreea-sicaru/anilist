package com.aissia.anilist.presentation.animelist

import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.presentation.UiState

object AnimeListContract {

    data class State(
        val title: String,
        val items: UiState<List<AnimePreview>> = UiState.Loading,
        val currentPage: Int = 1,
        val hasNextPage: Boolean = true,
        val isPaginating: Boolean = false,
    )

    sealed class Event {
        data class AnimeClicked(val animeId: Int) : Event()
        object LoadMore : Event()
        object Retry : Event()
        object OnBackClicked : Event()
    }

    sealed class Effect {
        data class NavigateToDetail(val animeId: Int) : Effect()
        object NavigateBack : Effect()
        data class ShowError(val message: String) : Effect()
    }
}
