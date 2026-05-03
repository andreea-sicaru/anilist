package com.aissia.anilist.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aissia.anilist.R
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.presentation.common.GenreChip
import com.aissia.anilist.presentation.common.GenreChipList
import com.aissia.anilist.presentation.common.RatingRow
import com.aissia.anilist.presentation.toFormattedDuration
import com.aissia.anilist.ui.theme.Dimens

@Composable
fun PopularAnimeCard(anime: Anime, onAnimeClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAnimeClick(anime.id) }
            .padding(horizontal = Dimens.PaddingLarge, vertical = 8.dp)
    ) {
        AsyncImage(
            model = anime.coverImageLarge,
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = Dimens.PopularPosterWidth, height = Dimens.PopularPosterHeight)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = anime.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            anime.averageScore?.let { score ->
                RatingRow(score)
            }

            GenreChipList(anime.genres, modifier = Modifier.padding(top = 4.dp))

            anime.duration?.let { minutes ->
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = minutes.toFormattedDuration(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}