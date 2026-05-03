package com.aissia.anilist.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.animelist.AnimeListType
import com.aissia.anilist.presentation.common.ErrorView
import com.aissia.anilist.presentation.home.components.HomeBottomBar
import com.aissia.anilist.presentation.home.components.HomeTopBar
import com.aissia.anilist.presentation.home.components.NowShowingSection
import com.aissia.anilist.presentation.home.components.PopularAnimeCard
import com.aissia.anilist.presentation.home.components.SectionHeader
import com.aissia.anilist.presentation.theme.Dimens
import com.aissia.anilist.presentation.theme.ScreenBackgroundLeft
import com.aissia.anilist.presentation.theme.ScreenBackgroundRight

@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    onSeeMore: (AnimeListType) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeContract.Effect.NavigateToDetail -> onAnimeClick(effect.animeId)
                is HomeContract.Effect.NavigateToList -> onSeeMore(effect.listType)
                is HomeContract.Effect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    HomeScreenContents(
        state = state,
        snackbarHostState = snackbarHostState,
        onAnimeClick = { viewModel.onEvent(HomeContract.Event.AnimeClicked(it)) },
        onSeeMore = { viewModel.onEvent(HomeContract.Event.SeeMore(it)) },
        onRetry = { viewModel.onEvent(HomeContract.Event.Retry) },
    )
}

@Composable
fun HomeScreenContents(
    state: HomeContract.State,
    snackbarHostState: SnackbarHostState,
    onAnimeClick: (Int) -> Unit,
    onSeeMore: (AnimeListType) -> Unit,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = ScreenBackgroundLeft, size = size.copy(width = size.width * 0.35f))
                drawRect(
                    color = ScreenBackgroundRight,
                    topLeft = Offset(x = size.width * 0.35f, y = 0f),
                    size = size.copy(width = size.width * 0.65f)
                )
            }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val data = state.homeSections) {
                    is UiState.Loading -> item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(Dimens.SpacingExtraLarge * 5),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }

                    is UiState.Error -> item {
                        ErrorView(message = data.message, onRetry = onRetry)
                    }

                    is UiState.Success -> {
                        item {
                            SectionHeader(
                                title = "Now showing",
                                onSeeMore = { onSeeMore(AnimeListType.NOW_SHOWING) },
                                modifier = Modifier.padding(
                                    horizontal = Dimens.PaddingLarge,
                                    vertical = Dimens.PaddingMedium
                                )
                            )
                        }
                        item {
                            NowShowingSection(
                                animes = data.data.nowShowing,
                                onAnimeClick = onAnimeClick
                            )
                        }
                        item { Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) }
                        item {
                            SectionHeader(
                                title = "Popular",
                                onSeeMore = { onSeeMore(AnimeListType.POPULAR) },
                                modifier = Modifier.padding(
                                    horizontal = Dimens.PaddingLarge,
                                    vertical = Dimens.PaddingMedium
                                )
                            )
                        }
                        items(data.data.popular) { anime ->
                            PopularAnimeCard(anime = anime, onAnimeClick = onAnimeClick)
                        }
                        item { Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) }
                    }
                }
            }
        }
    }
}
