package com.aissia.anilist.presentation.home

import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.animelist.AnimeListType

object HomeContract {

    data class State(
        val uiState: UiState<HomeSections> = UiState.Loading,
    )

    sealed class Event {
        data class AnimeClicked(val animeId: Int) : Event()
        data class SeeMore(val listType: AnimeListType) : Event()
        object Retry : Event()
    }

    sealed class Effect {
        data class NavigateToDetail(val animeId: Int) : Effect()
        data class NavigateToList(val listType: AnimeListType) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}
