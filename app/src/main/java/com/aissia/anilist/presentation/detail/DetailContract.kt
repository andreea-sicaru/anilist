package com.aissia.anilist.presentation.detail

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.presentation.UiState

object DetailContract {

    data class State(
        val detail: UiState<Anime> = UiState.Loading,
    )

    sealed class Event {
        object OnBackClicked : Event()
        object RetryLoad : Event()
    }

    sealed class Effect {
        object NavigateBack : Effect()
    }
}