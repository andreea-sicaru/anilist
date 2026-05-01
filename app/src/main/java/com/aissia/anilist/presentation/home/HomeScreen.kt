package com.aissia.anilist.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aissia.anilist.R
import com.aissia.anilist.ui.theme.AnilistTheme
import com.aissia.anilist.ui.theme.Merriweather

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    // Displaying a different colored rectangle underneath screen contents
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    color = Color(0xFFF9F9FA),
                    size = size.copy(width = size.width * 0.35f)
                )
                drawRect(
                    color = Color.White,
                    topLeft = Offset(x = size.width * 0.35f, y = 0f),
                    size = size.copy(width = size.width * 0.65f)
                )
            }
    ) {
        HomeScreenContents()
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContents() {
    AnilistTheme(dynamicColor = false) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { HomeTopBar() }
        ) { paddingValues ->
        }
    }
}

@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.menu_icon),
                contentDescription = "Menu",
                tint = Color.Unspecified
            )
        }
        Text(
            text = "FilmKu",
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.notification_icon),
                contentDescription = "Notifications",
                tint = Color.Unspecified
            )
        }
    }
}


