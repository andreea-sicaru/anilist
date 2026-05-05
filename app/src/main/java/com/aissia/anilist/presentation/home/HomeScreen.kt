package com.aissia.anilist.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aissia.anilist.R
import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.animelist.AnimeListType
import com.aissia.anilist.presentation.common.ErrorView
import com.aissia.anilist.presentation.common.LoadingView
import com.aissia.anilist.presentation.common.SectionHeader
import com.aissia.anilist.presentation.home.components.HomeTopBar
import com.aissia.anilist.presentation.home.components.NowShowingRow
import com.aissia.anilist.presentation.home.components.PopularAnimeCard
import com.aissia.anilist.presentation.placeholder.PreviewData
import com.aissia.anilist.presentation.theme.AnilistTheme
import com.aissia.anilist.presentation.theme.Dimens
import com.aissia.anilist.presentation.theme.ScreenBackgroundLeft
import com.aissia.anilist.presentation.theme.ScreenBackgroundRight

@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    onSeeMore: (AnimeListType) -> Unit,
    bottomBarHeight: Dp = 0.dp,
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
        bottomBarHeight = bottomBarHeight,
        snackbarHostState = snackbarHostState,
        onAnimeClick = { viewModel.onEvent(HomeContract.Event.AnimeClicked(it)) },
        onSeeMore = { viewModel.onEvent(HomeContract.Event.SeeMore(it)) },
        onRetry = { viewModel.onEvent(HomeContract.Event.Retry) },
    )
}

@Composable
fun HomeScreenContents(
    modifier: Modifier = Modifier,
    state: HomeContract.State,
    bottomBarHeight: Dp = 0.dp,
    snackbarHostState: SnackbarHostState,
    onAnimeClick: (Int) -> Unit,
    onSeeMore: (AnimeListType) -> Unit,
    onRetry: () -> Unit,
) {
    Box(modifier.fillMaxSize()) {

        HomeBackground()

        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = { HomeTopBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            when (val uiState = state.uiState) {
                is UiState.Loading -> LoadingView(modifier = Modifier.fillMaxSize())
                is UiState.Error -> ErrorView(
                    message = uiState.message,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )

                is UiState.Success<HomeSections> -> {
                    val data = uiState.data
                    LazyColumn(
                        modifier = Modifier.padding(paddingValues),
                        contentPadding = PaddingValues(bottom = bottomBarHeight),
                    ) {

                        data.nowShowing.isNotEmpty().let {
                            item {
                                SectionHeader(
                                    title = stringResource(R.string.section_now_showing),
                                    onSeeMore = { onSeeMore(AnimeListType.NOW_SHOWING) },
                                    modifier = modifier.padding(
                                        horizontal = Dimens.PaddingLarge,
                                        vertical = Dimens.PaddingMedium
                                    )
                                )
                            }
                            item {
                                NowShowingRow(
                                    animes = data.nowShowing,
                                    onAnimeClick = onAnimeClick
                                )
                            }

                            item { Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) }
                        }

                        data.popular.isNotEmpty().let {
                            item {
                                SectionHeader(
                                    title = stringResource(R.string.section_popular),
                                    onSeeMore = { onSeeMore(AnimeListType.POPULAR) },
                                    modifier = modifier.padding(
                                        horizontal = Dimens.PaddingLarge,
                                        vertical = Dimens.PaddingMedium
                                    )
                                )
                            }
                            items(data.popular) { anime ->
                                PopularAnimeCard(anime = anime, onAnimeClick = onAnimeClick)
                            }
                        }

                    }


                }
            }
        }
    }

}

@Composable
fun HomeBackground(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight()
                .background(ScreenBackgroundLeft)
        )
        Box(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
                .background(ScreenBackgroundRight)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AnilistTheme {
        HomeScreenContents(
            state = HomeContract.State(
                uiState = UiState.Success(
                    data = HomeSections(
                        nowShowing = PreviewData.animePreviewList,
                        popular = PreviewData.animePreviewList
                    )
                ),
            ),
            snackbarHostState = SnackbarHostState(),
//            state = HomeContract.State(uiState = UiState.Loading),
//            state = HomeContract.State(uiState = UiState.Error("Error")),
            onRetry = {},
            onAnimeClick = {},
            onSeeMore = {}
        )
    }
}
