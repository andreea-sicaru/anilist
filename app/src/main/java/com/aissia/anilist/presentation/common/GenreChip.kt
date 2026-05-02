package com.aissia.anilist.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissia.anilist.ui.theme.ChipBackground
import com.aissia.anilist.ui.theme.ChipText

@Composable
fun GenreChip(genre: String) {
    Box(
        modifier = Modifier
            .height(18.dp)
            .background(color = ChipBackground, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = ChipText
        )
    }
}