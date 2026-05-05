package com.aissia.anilist.presentation.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aissia.anilist.R
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.Character
import com.aissia.anilist.presentation.common.GenreChipList
import com.aissia.anilist.presentation.common.RatingRow
import com.aissia.anilist.presentation.common.SectionHeader
import com.aissia.anilist.presentation.theme.DarkBlue900
import com.aissia.anilist.presentation.theme.Dimens
import com.aissia.anilist.presentation.theme.LightGray100
import com.aissia.anilist.presentation.toFormattedDuration
import com.aissia.anilist.presentation.toLanguage

@Composable
fun AnimeInfoSection(anime: Anime) {
    var isBookmarked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
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
        RatingRow(score = score, modifier = Modifier.padding(top = Dimens.PaddingSmall))
    }

    GenreChipList(anime.genres, modifier = Modifier.padding(top = Dimens.PaddingMedium))

    MetaRow(anime = anime, modifier = Modifier.padding(top = Dimens.PaddingMedium))

    anime.description?.let { description ->
        SectionHeader(
            title = stringResource(R.string.label_description),
            modifier = Modifier.padding(top = Dimens.PaddingLarge)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelMedium,
            color = LightGray100,
            lineHeight = 22.sp,
            letterSpacing = (12 * 0.02).sp,
            modifier = Modifier.padding(top = Dimens.PaddingSmall),
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
        MetaItem(stringResource(R.string.meta_length), duration ?: stringResource(R.string.meta_unknown), Modifier.weight(1f))
        MetaItem(stringResource(R.string.meta_language), anime.countryOfOrigin.toLanguage(), Modifier.weight(1f))
        val rating = stringResource(if (anime.isAdult) R.string.meta_rating_r else R.string.meta_rating_pg13)
        MetaItem(stringResource(R.string.meta_rating), rating, Modifier.weight(1f))
    }
}

@Composable
private fun MetaItem(title: String, subtitle: String, modifier: Modifier = Modifier) {
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

