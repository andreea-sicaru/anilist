package com.aissia.anilist.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aissia.anilist.R
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.Character
import com.aissia.anilist.domain.model.MediaStatus
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.common.ErrorView
import com.aissia.anilist.presentation.common.LoadingView
import com.aissia.anilist.presentation.detail.components.AnimeInfoSection
import com.aissia.anilist.presentation.detail.components.BannerSection
import com.aissia.anilist.presentation.theme.AnilistTheme

@Composable
fun DetailScreen(
    onBack: () -> Unit,
    bottomBarHeight: Dp = 0.dp,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailContract.Effect.NavigateBack -> onBack()
            }
        }
    }

    DetailScreenContent(
        state = state,
        bottomBarHeight = bottomBarHeight,
        onBack = { viewModel.onEvent(DetailContract.Event.OnBackClicked) },
        onRetry = { viewModel.onEvent(DetailContract.Event.RetryLoad) },
    )
}

@Composable
fun DetailScreenContent(
    state: DetailContract.State,
    onBack: () -> Unit,
    bottomBarHeight: Dp = 0.dp,
    onRetry: () -> Unit = {},
) {
    when (val detail = state.detail) {
        is UiState.Loading -> LoadingView(Modifier.fillMaxSize())
        is UiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) { ErrorView(message = detail.message, onRetry = onRetry) }
        is UiState.Success -> AnimeContent(anime = detail.data, onBack = onBack, bottomBarHeight = bottomBarHeight)
    }
}

@Composable
private fun AnimeContent(anime: Anime, onBack: () -> Unit, bottomBarHeight: Dp = 0.dp) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            BannerSection(bannerUrl = anime.bannerImage ?: "", trailer = anime.trailer)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                AnimeInfoSection(anime)
            }
            Spacer(modifier = Modifier.height(bottomBarHeight))
        }
        DetailTopBar(onBack = onBack)
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = stringResource(R.string.cd_more),
                    tint = Color.White,
                )
            }
        }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenPreview() {
    AnilistTheme(dynamicColor = false) {
        DetailScreenContent(
            state = DetailContract.State(
                detail = UiState.Success(Anime(
                    id = 1,
                    title = "Spiderman: No Way Home",
                    bannerImage = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/250-JpXhinXPqpNE.jpg",
                    coverImageLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b250-w0c2KefXfW2i.png",
                    coverImageExtraLarge = null,
                    coverImageColor = "#e4ae50",
                    genres = listOf("Action", "Adventure", "Fantasy"),
                    averageScore = 91.0,
                    popularity = 50000,
                    description = "With Spider-Man's identity now revealed, Peter asks Doctor Strange for help.",
                    status = MediaStatus.RELEASING,
                    seasonYear = 2021,
                    trailer = null,
                    duration = 170,
                    isAdult = false,
                    countryOfOrigin = "JP",
                    characters = listOf(
                        Character(id = 1, name = "Spider-Man", imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b270810-RDnZzM4DtLyn.png", role = "MAIN"),
                        Character(id = 2, name = "Doctor Strange", imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b270810-RDnZzM4DtLyn.png", role = "SUPPORTING"),
                    )
                )
            )),
            onBack = {}
        )
    }
}
