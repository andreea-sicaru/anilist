package com.aissia.anilist.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aissia.anilist.domain.usecase.GetNowShowingAnimeUseCase
import com.aissia.anilist.domain.usecase.GetPopularAnimeUseCase
import com.aissia.anilist.presentation.toErrorMessage
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
    private val getPopularAnime: GetPopularAnimeUseCase,
    private val getNowShowingAnime: GetNowShowingAnimeUseCase
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
            is HomeContract.Event.AnimeClicked -> sendEffect(HomeContract.Effect.NavigateToDetail(event.animeId))
            is HomeContract.Event.LoadMorePopular -> loadMorePopular()
            is HomeContract.Event.RetryPopular -> loadPopular(page = 1, reset = true)
            is HomeContract.Event.RetryTrending -> loadNowShowing()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingPopular = true, isLoadingNowShowing = true) }

            val popularDeferred = async { getPopularAnime(page = 1) }
            val nowShowingDeferred = async { getNowShowingAnime(page = 1) }

            nowShowingDeferred.await().fold(
                onSuccess = { (list, hasNext) ->  _state.update { it.copy(isLoadingNowShowing = false, nowShowingAnime = list, nowShowingError = null) } },
                onFailure = { e -> _state.update { it.copy(isLoadingNowShowing = false, nowShowingError = e.toErrorMessage()) } }
            )
            popularDeferred.await().fold(onSuccess = { (list, hasNext) ->
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
                    it.copy(isLoadingPopular = false, popularError = e.toErrorMessage())
                }
            })
        }
    }

    private fun loadNowShowing() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingNowShowing = true, nowShowingError = null) }
            getNowShowingAnime(page = 1).fold(
                onSuccess = { (list, _) ->
                    _state.update { it.copy(isLoadingNowShowing = false, nowShowingAnime = list) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoadingNowShowing = false, nowShowingError = e.toErrorMessage()) }
                    sendEffect(HomeContract.Effect.ShowError(e.toErrorMessage()))
                }
            )
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
                    _state.update { it.copy(isPaginatingPopular = false, popularError = e.toErrorMessage()) }
                    sendEffect(HomeContract.Effect.ShowError(e.toErrorMessage()))
                }
            )
        }
    }

    private fun sendEffect(effect: HomeContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

}