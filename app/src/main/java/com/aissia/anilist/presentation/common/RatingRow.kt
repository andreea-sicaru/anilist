package com.aissia.anilist.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aissia.anilist.R
import com.aissia.anilist.presentation.theme.GoldStar
import com.aissia.anilist.presentation.theme.LightGray100

@Composable
fun RatingRow(score: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = GoldStar,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = stringResource(R.string.imdb_rating, "%.1f".format(score)),
            style = MaterialTheme.typography.labelMedium,
            color = LightGray100
        )
    }
}