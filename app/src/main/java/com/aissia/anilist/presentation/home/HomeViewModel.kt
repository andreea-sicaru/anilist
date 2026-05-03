package com.aissia.anilist.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aissia.anilist.domain.usecase.GetHomeSectionsUseCase
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
class HomeViewModel @Inject constructor(
    private val getSectionsData: GetHomeSectionsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeContract.State())
    val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    private val _effect = Channel<HomeContract.Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadHomeData()
    }

    fun onEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.AnimeClicked -> sendEffect(HomeContract.Effect.NavigateToDetail(event.animeId))
            is HomeContract.Event.SeeMore -> sendEffect(HomeContract.Effect.NavigateToList(event.listType))
            is HomeContract.Event.Retry -> loadHomeData()
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _state.update { it.copy(homeSections = UiState.Loading) }
            getSectionsData().fold(
                onSuccess = { data ->
                    _state.update { it.copy(homeSections = UiState.Success(data)) }
                },
                onFailure = { e ->
                    val message = e.toErrorMessage()
                    _state.update { it.copy(homeSections = UiState.Error(message)) }
                    sendEffect(HomeContract.Effect.ShowError(message))
                }
            )
        }
    }

    private fun sendEffect(effect: HomeContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
