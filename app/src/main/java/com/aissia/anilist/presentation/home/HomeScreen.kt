package com.aissia.anilist.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aissia.anilist.R
import com.aissia.anilist.ui.theme.AnilistTheme
import com.aissia.anilist.ui.theme.DarkBlue900

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    HomeScreenContents()
}

@Composable
private fun HomeScreenContents() {
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
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomBar() }
        ) { paddingValues ->
        }
    }
}

@Composable
fun HomeBottomBar(modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                clip = false
            ),
        tonalElevation = 0.dp,
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = true, onClick = { }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_movie_reel),
                    contentDescription = "Home",
                    tint = Color.Unspecified,
                    modifier = modifier
                        .size(24.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue900,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(selected = false, onClick = {}, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_ticket),
                contentDescription = "Tickets",
                tint = Color.Unspecified,
                modifier = modifier.size(24.dp)
            )
        })

        NavigationBarItem(selected = false, onClick = {}, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_saved),
                contentDescription = "Saved",
                tint = Color.Unspecified,
                modifier = modifier.size(24.dp)
            )
        })
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
                painter = painterResource(id = R.drawable.ic_menu),
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
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "Notifications",
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentsPreview() {
    AnilistTheme(dynamicColor = false) {
        HomeScreenContents()
    }
}


