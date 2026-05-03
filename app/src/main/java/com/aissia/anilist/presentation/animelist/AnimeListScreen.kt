package com.aissia.anilist.presentation.animelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.common.ErrorView
import com.aissia.anilist.presentation.home.components.PopularAnimeCard
import com.aissia.anilist.presentation.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    onBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    viewModel: AnimeListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AnimeListContract.Effect.NavigateToDetail -> onAnimeClick(effect.animeId)
                is AnimeListContract.Effect.NavigateBack -> onBack()
                is AnimeListContract.Effect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.onEvent(AnimeListContract.Event.LoadMore)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = state.title, style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AnimeListContract.Event.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState
        ) {
            when (val items = state.items) {
                is UiState.Loading -> item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(Dimens.SpacingExtraLarge * 5),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                is UiState.Error -> item {
                    ErrorView(message = items.message, onRetry = { viewModel.onEvent(AnimeListContract.Event.Retry) })
                }

                is UiState.Success -> {
                    items(items.data) { anime ->
                        PopularAnimeCard(anime = anime, onAnimeClick = onAnimeClick)
                    }
                    if (state.isPaginating) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(Dimens.PaddingMedium),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) }
                }
            }
        }
    }
}
