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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.presentation.home.components.HomeBottomBar
import com.aissia.anilist.presentation.home.components.HomeTopBar
import com.aissia.anilist.presentation.home.components.NowShowingSection
import com.aissia.anilist.presentation.home.components.PopularAnimeCard
import com.aissia.anilist.presentation.home.components.SectionHeader
import com.aissia.anilist.ui.theme.AnilistTheme
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
                item { NowShowingSection(animes = placeholderAnimes) }
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
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
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

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentsPreview() {
    AnilistTheme(dynamicColor = false) {
        HomeScreenContents(state = HomeContract.State(isLoadingPopular = true), listState = LazyListState())
    }
}

// TODO: remove once ViewModel provides real data
private val placeholderAnime = Anime(
    id = 1,
    title = "Spiderman: No Way Home",
    coverImageLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b250-w0c2KefXfW2i.png",
    coverImageExtraLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b250-w0c2KefXfW2i.png",
    coverImageColor = "#e4ae50",
    genres = listOf("Adventure", "Comedy", "Supernatural"),
    bannerImage = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/250-JpXhinXPqpNE.jpg",
    averageScore = 73.0,
    popularity = 27325,
    description = "Takamine Kiyomaro, a depressed don't-care-about-the-world guy, was suddenly given a little demon named Gash Bell to take care of.",
    status = "FINISHED",
    seasonYear = 2003,
    trailer = null
)
private val placeholderAnimes = listOf(
    placeholderAnime,
    placeholderAnime.copy(id = 2, title = "Venom Let There Be Carnage Venom Let There Be Carnage"),
    placeholderAnime.copy(id = 3)
)