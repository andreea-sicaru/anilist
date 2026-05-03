package com.aissia.anilist.presentation.detail

import com.aissia.anilist.domain.model.Anime


object DetailContract {

    data class State(
        val isLoading: Boolean = true,
        val anime: Anime? = null,
        val error: String? = null
    )

    sealed class Event {
        data class LoadDetail(val animeId: Int) : Event()
        object OnBackClicked : Event()
        object RetryLoad : Event()
    }

    sealed class Effect {
        object NavigateBack : Effect()
    }
}