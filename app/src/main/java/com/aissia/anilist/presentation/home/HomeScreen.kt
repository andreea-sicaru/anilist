package com.aissia.anilist.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aissia.anilist.presentation.home.components.HomeBottomBar
import com.aissia.anilist.presentation.home.components.HomeTopBar
import com.aissia.anilist.presentation.home.components.NowShowingSection
import com.aissia.anilist.presentation.home.components.PopularAnimeCard
import com.aissia.anilist.presentation.home.components.SectionHeader
import com.aissia.anilist.ui.theme.Dimens
import com.aissia.anilist.ui.theme.ScreenBackgroundLeft
import com.aissia.anilist.ui.theme.ScreenBackgroundRight

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeContract.Effect.ShowError -> {}
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
        if (shouldLoadMore) viewModel.onEvent(HomeContract.Event.LoadMorePopular)
    }

    HomeScreenContents(state, listState)
}

@Composable
fun HomeScreenContents(state: HomeContract.State, listState: LazyListState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    color = ScreenBackgroundLeft,
                    size = size.copy(width = size.width * 0.35f)
                )
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
            bottomBar = { HomeBottomBar() }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = listState
            ) {
                item { SectionHeader(
                    title = "Now showing",
                    modifier = Modifier.padding(
                        horizontal = Dimens.PaddingLarge,
                        vertical = Dimens.PaddingMedium
                    )

                ) }

                if (state.isLoadingNowShowing) {
                    item { LoadingIndicator() }
                } else {
                    item { NowShowingSection(animes = state.nowShowingAnime) }
                }

                item { Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) }

                item {
                    SectionHeader(
                        title = "Popular",
                        modifier = Modifier.padding(
                            horizontal = Dimens.PaddingLarge,
                            vertical = Dimens.PaddingMedium
                        )
                    )
                }

                if (state.isLoadingPopular) {
                    item { LoadingIndicator() }
                } else {
                    items(state.popularAnime) { anime ->
                        PopularAnimeCard(anime = anime)
                    }

                    if (state.isPaginatingPopular) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(Dimens.PaddingMedium), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}