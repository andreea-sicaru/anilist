package com.aissia.anilist.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aissia.anilist.R
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.Character
import com.aissia.anilist.domain.model.MediaStatus
import com.aissia.anilist.domain.model.Trailer
import com.aissia.anilist.presentation.common.ErrorView
import com.aissia.anilist.presentation.common.GenreChipList
import com.aissia.anilist.presentation.common.RatingRow
import com.aissia.anilist.presentation.detail.components.TrailerPlayer
import com.aissia.anilist.presentation.common.SectionHeader
import com.aissia.anilist.presentation.theme.AnilistTheme
import com.aissia.anilist.presentation.theme.DarkBlue900
import com.aissia.anilist.presentation.theme.Dimens
import com.aissia.anilist.presentation.theme.LightGray100
import com.aissia.anilist.presentation.toFormattedDuration
import com.aissia.anilist.presentation.toLanguage

@Composable
fun DetailScreen(
    onBack: () -> Unit,
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
        onBack = { viewModel.onEvent(DetailContract.Event.OnBackClicked) },
        onRetry = { viewModel.onEvent(DetailContract.Event.RetryLoad) },
    )
}

@Composable
fun DetailScreenContent(
    state: DetailContract.State,
    onBack: () -> Unit,
    onRetry: () -> Unit = {},
) {
    when {
        state.isLoading -> LoadingContent()
        state.error != null -> ErrorContent(message = state.error, onRetry = onRetry)
        state.anime != null -> AnimeContent(anime = state.anime, onBack = onBack)
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ErrorView(message = message, onRetry = onRetry)
    }
}

@Composable
private fun AnimeContent(anime: Anime, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScrollableContent(anime)
        DetailTopBar(onBack = onBack)
    }
}

@Composable
private fun ScrollableContent(anime: Anime) {
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
            DescriptionSection(anime)
        }
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)
                )
            )
    ) {
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
}

@Composable
private fun BannerSection(bannerUrl: String, trailer: Trailer?) {
    var isPlayingTrailer by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (isPlayingTrailer) {
            TrailerPlayer(trailer = trailer, modifier = Modifier.fillMaxSize())
        } else {
            AsyncImage(
                model = bannerUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.15f),
                            )
                        )
                    )
            )
            if (trailer != null) {
                PlayButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { isPlayingTrailer = true }
                )
            }
        }
    }
}

@Composable
private fun PlayButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.play_trailer),
                    tint = DarkBlue900,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Text(
            text = stringResource(R.string.play_trailer),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DescriptionSection(anime: Anime, modifier: Modifier = Modifier) {
    var isBookmarked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = anime.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Icon(
            modifier = Modifier.clickable { isBookmarked = !isBookmarked },
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
            contentDescription = stringResource(R.string.cd_bookmark),
            tint = if (isBookmarked) DarkBlue900 else MaterialTheme.colorScheme.onSurface,
        )
    }

    anime.averageScore?.let { score ->
        RatingRow(score = score, modifier = modifier.padding(top = Dimens.PaddingSmall))
    }

    GenreChipList(anime.genres, modifier = Modifier.padding(top = Dimens.PaddingMedium))

    MetaRow(anime = anime, modifier = Modifier.padding(top = Dimens.PaddingMedium))

    anime.description?.let {
        SectionHeader(
            title = stringResource(R.string.label_description),
            modifier = modifier.padding(top = Dimens.PaddingLarge)
        )
        Text(
            text = it,
            style = MaterialTheme.typography.labelMedium,
            color = LightGray100,
            lineHeight = 22.sp,
            letterSpacing = (12 * 0.02).sp,
            modifier = modifier.padding(top = Dimens.PaddingSmall),
        )
    }

    if (anime.characters.isNotEmpty()) {
        SectionHeader(
            title = stringResource(R.string.label_cast),
            onSeeMore = {},
            modifier = Modifier.padding(top = Dimens.PaddingLarge),
        )
        CastList(
            characters = anime.characters,
            modifier = Modifier.padding(top = Dimens.PaddingMedium)
        )
    }
}

@Composable
private fun MetaRow(anime: Anime, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val duration = anime.duration?.toFormattedDuration()
        Meta(stringResource(R.string.meta_length), duration ?: stringResource(R.string.meta_unknown), Modifier.weight(1f))
        Meta(stringResource(R.string.meta_language), anime.countryOfOrigin.toLanguage(), Modifier.weight(1f))
        val rating = stringResource(if (anime.isAdult) R.string.meta_rating_r else R.string.meta_rating_pg13)
        Meta(stringResource(R.string.meta_rating), rating, Modifier.weight(1f))
    }
}

@Composable
private fun Meta(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingTiny)
    ) {
        Text(text = title, style = MaterialTheme.typography.labelMedium, color = LightGray100)
        Text(text = subtitle, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun CastList(characters: List<Character>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
    ) {
        repeat(4) { index ->
            val character = characters.getOrNull(index)
            Box(modifier = Modifier.weight(1f)) {
                if (character != null) CastCard(character)
            }
        }
    }
}

@Composable
private fun CastCard(character: Character) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingTiny),
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        )
        Text(
            text = character.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenPreview() {
    AnilistTheme(dynamicColor = false) {
        DetailScreenContent(
            state = DetailContract.State(
                isLoading = false,
                anime = Anime(
                    id = 1,
                    title = "Spiderman: No Way Home : No Way Home",
                    bannerImage = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/250-JpXhinXPqpNE.jpg",
                    coverImageLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b250-w0c2KefXfW2i.png",
                    coverImageExtraLarge = null,
                    coverImageColor = "#e4ae50",
                    genres = listOf("Action", "Adventure", "Fantasy"),
                    averageScore = 91.0,
                    popularity = 50000,
                    description = "With Spider-Man's identity now revealed, Peter asks Doctor Strange for help. When a spell goes wrong, dangerous foes from other worlds start to appear, forcing Peter to discover what it truly means to be Spider-Man.",
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
            ),
            onBack = {}
        )
    }
}
