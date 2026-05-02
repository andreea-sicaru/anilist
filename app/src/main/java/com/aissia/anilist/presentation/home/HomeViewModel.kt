package com.aissia.anilist.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aissia.anilist.domain.usecase.GetPopularAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    private val getPopularAnime: GetPopularAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeContract.State())
    val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    private val _effect = Channel<HomeContract.Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(HomeContract.Event.LoadInitialData)
    }

    fun onEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.LoadInitialData -> loadInitialData()
            is HomeContract.Event.LoadMorePopular -> loadMorePopular()
            is HomeContract.Event.RetryPopular -> {}
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingPopular = true) }

            val popularDeffered = async { getPopularAnime(page = 1) }

            popularDeffered.await().fold(onSuccess = { (list, hasNext) ->
                _state.update {
                    it.copy(
                        isLoadingPopular = false,
                        popularAnime = list,
                        hasMorePopular = hasNext,
                        currentPopularPage = 1,
                        popularError = null
                    )
                }
            }, onFailure = { e ->
                _state.update {
                    it.copy(
                        isLoadingPopular = false, popularError = e.message
                    )
                }
            })
        }
    }

    private fun loadMorePopular() {
        val current = _state.value
        if (!current.hasMorePopular || current.isPaginatingPopular) return
        loadPopular(page = current.currentPopularPage + 1, reset = false)
    }

    private fun loadPopular(page: Int, reset: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isPaginatingPopular = true, popularError = null) }
            getPopularAnime(page = page).fold(
                onSuccess = { (list, hasNext) ->
                    _state.update { s ->
                        s.copy(
                            isPaginatingPopular = false,
                            isLoadingPopular = false,
                            popularAnime = if (reset) list else s.popularAnime + list,
                            hasMorePopular = hasNext,
                            currentPopularPage = page,
                            popularError = null
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isPaginatingPopular = false, popularError = e.message) }
                    sendEffect(HomeContract.Effect.ShowError(e.message ?: "Unknown error"))
                }
            )
        }
    }

    private fun sendEffect(effect: HomeContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

}