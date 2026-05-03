package com.aissia.anilist.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aissia.anilist.presentation.theme.DarkBlue900
import com.aissia.anilist.presentation.theme.LightGray80

@Composable
fun SectionHeader(title: String, onSeeMore: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = DarkBlue900
        )
        if (onSeeMore != null) {
            OutlinedButton(
                onClick = onSeeMore,
                modifier = Modifier.height(21.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "See more",
                    style = MaterialTheme.typography.labelSmall,
                    color = LightGray80
                )
            }
        }
    }
}