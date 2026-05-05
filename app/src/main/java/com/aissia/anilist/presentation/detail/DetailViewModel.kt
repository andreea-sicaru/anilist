package com.aissia.anilist.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aissia.anilist.domain.repository.AnimeRepository
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Navigation 2.8 serialises DetailRoute into SavedStateHandle as individual typed properties,
    // so reading "animeId" directly is equivalent to toRoute<DetailRoute>().animeId and
    // works in both production and JVM unit tests without requiring Android Bundle mocking.
    private val animeId: Int = checkNotNull(savedStateHandle["animeId"])

    private val _state = MutableStateFlow(DetailContract.State())
    val state: StateFlow<DetailContract.State> = _state.asStateFlow()

    private val _effect = Channel<DetailContract.Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadDetail()
    }

    fun onEvent(event: DetailContract.Event) {
        when (event) {
            is DetailContract.Event.OnBackClicked -> sendEffect(DetailContract.Effect.NavigateBack)
            is DetailContract.Event.RetryLoad -> loadDetail()
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.update { it.copy(detail = UiState.Loading) }
            repository.getAnimeDetail(animeId).fold(
                onSuccess = { anime -> _state.update { it.copy(detail = UiState.Success(anime)) } },
                onFailure = { e -> _state.update { it.copy(detail = UiState.Error(e.toErrorMessage())) } }
            )
        }
    }

    private fun sendEffect(effect: DetailContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
