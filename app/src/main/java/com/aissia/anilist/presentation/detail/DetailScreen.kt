package com.aissia.anilist.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MoreHoriz
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.Character
import com.aissia.anilist.domain.model.Trailer
import com.aissia.anilist.presentation.common.GenreChipList
import com.aissia.anilist.presentation.common.RatingRow
import com.aissia.anilist.presentation.detail.components.TrailerPlayer
import com.aissia.anilist.presentation.home.DetailViewModel
import com.aissia.anilist.presentation.home.components.SectionHeader
import com.aissia.anilist.presentation.toFormattedDuration
import com.aissia.anilist.presentation.toLanguage
import com.aissia.anilist.ui.theme.AnilistTheme
import com.aissia.anilist.ui.theme.DarkBlue900
import com.aissia.anilist.ui.theme.Dimens
import com.aissia.anilist.ui.theme.LightGray100

@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailContract.Effect.NavigateBack -> onBack()
                is DetailContract.Effect.ShowError -> {}
            }
        }
    }

    DetailScreenContent(
        state = state,
        onBack = { viewModel.onEvent(DetailContract.Event.OnBackClicked) }
    )
}

@Composable
fun DetailScreenContent(
    state: DetailContract.State,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        BannerSection(
            bannerUrl = state.anime?.bannerImage ?: "",
            trailer = state.anime?.trailer,
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-24).dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            DescriptionSection(state.anime)
        }
    }
}

@Composable
fun DescriptionSection(anime: Anime?, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = anime?.title ?: "",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Icon(
            imageVector = Icons.Outlined.BookmarkBorder,
            contentDescription = "Bookmark",
            modifier = Modifier
//                .clickable { }
                .padding(top = 3.dp)
        )
    }

    anime?.averageScore?.let { score ->
        RatingRow(score = score, modifier = modifier.padding(top = Dimens.PaddingSmall))
    }

    anime?.genres?.let {
        GenreChipList(it, modifier = Modifier.padding(top = Dimens.PaddingMedium))
    }

    anime?.let { MetaRow(anime, modifier = Modifier.padding(top = Dimens.PaddingSmall)) }

    anime?.description?.let {
        Text(
            modifier = modifier.padding(top = Dimens.PaddingLarge),
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            color = DarkBlue900
        )
        Text(
            modifier = modifier.padding(top = Dimens.PaddingSmall),
            text = it,
            style = MaterialTheme.typography.labelMedium,
            color = LightGray100,
            lineHeight = 22.sp,
            letterSpacing = (12 * 0.02).sp
        )
    }

    if (!anime?.characters.isNullOrEmpty()) {
        SectionHeader("Cast", modifier = Modifier.padding(top = Dimens.PaddingLarge))
        CastList(
            characters = anime!!.characters,
            modifier = Modifier.padding(top = Dimens.PaddingMedium)
        )
    }
}

@Composable
fun MetaRow(
    anime: Anime, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val duration = anime.duration?.toFormattedDuration()
        Meta("Length", duration ?: "Unkown", modifier.weight(1f))

        Meta("Language", anime.countryOfOrigin.toLanguage(), modifier.weight(1f))
        Meta("Rating", "PG-13", modifier.weight(1f))
    }

}

@Composable
fun Meta(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingTiny)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CastList(characters: List<Character>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
    ) {
        characters.take(4).forEach { character ->
            CastCard(character, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CastCard(character: Character, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingTiny),
        modifier = modifier,
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

@Composable
private fun BannerSection(
    bannerUrl: String,
    trailer: Trailer?,
    onBack: () -> Unit,
) {
    var isPlayingTrailer by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (isPlayingTrailer) {
            TrailerPlayer(
                trailer = trailer,
                modifier = Modifier.fillMaxSize()
            )
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

        // Always on top regardless of banner/player state
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
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "More",
                    tint = Color.White,
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
                    contentDescription = "Play Trailer",
                    tint = DarkBlue900,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Text(
            text = "Play Trailer",
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true,
    device = "spec:width=375dp,height=812dp,dpi=460" // to match Figma
)
@Composable
private fun DetailScreenPreview() {
    AnilistTheme(dynamicColor = false) {
        DetailScreenContent(
            state = DetailContract.State(
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
                    status = "RELEASING",
                    seasonYear = 2021,
                    trailer = null,
                    duration = 170,
                    countryOfOrigin = "JP",
                    characters = listOf(
                        Character(
                            id = 1, name = "Copy",
                            imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b270810-RDnZzM4DtLyn.png",
                            role = "Main"
                        ),
                        Character(
                            id = 1, name = "Copy",
                            imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b270810-RDnZzM4DtLyn.png",
                            role = "Main"
                        ),
                        Character(
                            id = 1, name = "Copy",
                            imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b270810-RDnZzM4DtLyn.png",
                            role = "Main"
                        ),
                        Character(
                            id = 1, name = "Copy",
                            imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b270810-RDnZzM4DtLyn.png",
                            role = "Main"
                        )
                    )
                )
            ),
            onBack = {}
        )
    }
}
