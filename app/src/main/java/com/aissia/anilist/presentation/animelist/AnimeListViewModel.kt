package com.aissia.anilist.presentation.animelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.PaginatedResult
import com.aissia.anilist.domain.repository.AnimeRepository
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.navigation.AnimeListRoute
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
class AnimeListViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val listType: AnimeListType = savedStateHandle.toRoute<AnimeListRoute>().listType

    private val _state = MutableStateFlow(AnimeListContract.State(listType = listType))
    val state: StateFlow<AnimeListContract.State> = _state.asStateFlow()

    private val _effect = Channel<AnimeListContract.Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadPage(page = 1, reset = true)
    }

    fun onEvent(event: AnimeListContract.Event) {
        when (event) {
            is AnimeListContract.Event.AnimeClicked -> sendEffect(AnimeListContract.Effect.NavigateToDetail(event.animeId))
            is AnimeListContract.Event.OnBackClicked -> sendEffect(AnimeListContract.Effect.NavigateBack)
            is AnimeListContract.Event.LoadMore -> loadMore()
            is AnimeListContract.Event.Retry -> loadPage(page = 1, reset = true)
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (!current.hasNextPage || current.isPaginating) return
        loadPage(page = current.currentPage + 1, reset = false)
    }

    private fun loadPage(page: Int, reset: Boolean) {
        viewModelScope.launch {
            if (reset) {
                _state.update { it.copy(items = UiState.Loading) }
            } else {
                _state.update { it.copy(isPaginating = true) }
            }

            fetchPage(page).fold(
                onSuccess = { result ->
                    _state.update { s ->
                        val existing = (s.items as? UiState.Success)?.data ?: emptyList()
                        s.copy(
                            items = UiState.Success(if (reset) result.items else existing + result.items),
                            currentPage = page,
                            hasNextPage = result.hasNextPage,
                            isPaginating = false,
                        )
                    }
                },
                onFailure = { e ->
                    val message = e.toErrorMessage()
                    if (reset) {
                        _state.update { it.copy(items = UiState.Error(message)) }
                    } else {
                        _state.update { it.copy(isPaginating = false) }
                        sendEffect(AnimeListContract.Effect.ShowError(message))
                    }
                }
            )
        }
    }

    private suspend fun fetchPage(page: Int): Result<PaginatedResult<AnimePreview>> = when (listType) {
        AnimeListType.NOW_SHOWING -> repository.getNowShowingAnime(page, perPage = 20)
        AnimeListType.POPULAR -> repository.getPopularAnime(page, perPage = 20)
    }

    private fun sendEffect(effect: AnimeListContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
