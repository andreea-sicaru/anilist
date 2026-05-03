package com.aissia.anilist.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aissia.anilist.domain.usecase.GetAnimeDetailUseCase
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
    private val getAnimeDetail: GetAnimeDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

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
            is DetailContract.Event.LoadDetail -> loadDetail()
            is DetailContract.Event.OnBackClicked -> sendEffect(DetailContract.Effect.NavigateBack)
            is DetailContract.Event.RetryLoad -> loadDetail()
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getAnimeDetail(animeId).fold(
                onSuccess = { anime -> _state.update { it.copy(isLoading = false, anime = anime) } },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.toErrorMessage()) }
                }
            )
        }
    }

    private fun sendEffect(effect: DetailContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}