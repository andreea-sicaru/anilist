package com.aissia.anilist.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.ui.theme.Dimens
import com.aissia.anilist.ui.theme.GoldStar
import com.aissia.anilist.ui.theme.LightGray100

@Composable
fun NowShowingSection(animes: List<Anime>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.PaddingLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            items(animes) { anime ->
                NowShowingCard(anime = anime, onClick = {})
            }
        }
    }
}

@Composable
fun NowShowingCard(anime: Anime, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(Dimens.NowShowingCardWidth),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Card(
            modifier = Modifier
                .size(width = Dimens.NowShowingCardWidth, height = Dimens.NowShowingCardHeight)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(Dimens.RadiusMedium), clip = false),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {
            AsyncImage(
                model = anime.coverImageExtraLarge ?: anime.coverImageLarge,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = anime.title,
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        anime.averageScore?.let { score ->
            RatingRow(score = score)
        }
    }
}
