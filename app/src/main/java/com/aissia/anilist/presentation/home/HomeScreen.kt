package com.aissia.anilist.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aissia.anilist.R
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.ui.theme.AnilistTheme
import com.aissia.anilist.ui.theme.DarkBlue900
import com.aissia.anilist.ui.theme.GoldStar
import com.aissia.anilist.ui.theme.LightGray100
import com.aissia.anilist.ui.theme.LightGray80

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
                    color = Color(0xFFF9F9FA), size = size.copy(width = size.width * 0.35f)
                )
                drawRect(
                    color = Color.White,
                    topLeft = Offset(x = size.width * 0.35f, y = 0f),
                    size = size.copy(width = size.width * 0.65f)
                )
            }) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomBar() }

        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
//                item {  }
                item { NowShowingSection() }

                item { SectionHeader(title = "Popular") }
                item { PopularSection() }
            }
        }
    }
}

@Composable
fun NowShowingSection(modifier: Modifier = Modifier) {
    val anime = Anime(
        1,
        title = "Spiderman: No Way Home",
        coverImageLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b250-w0c2KefXfW2i.png",
        coverImageExtraLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b250-w0c2KefXfW2i.png",
        coverImageColor = "#e4ae50",
        genres = listOf("Adventure", "Comedy", "Supernatural"),
        bannerImage = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/250-JpXhinXPqpNE.jpg",
        averageScore = 73.0,
        popularity = 27325,
        description = "Takamine Kiyomaro, a depressed don't-care-about-the-world guy, was suddenly given a little demon named Gash Bell to take care of. Little does he know that Gash is embroiled into an intense fight to see who is the ruler of the demon world. All of the demons have to pick a master on Earth and duke it out with other demons until one survives. Needless to say, Kiyomaro becomes Gash's master, and through their many battles, Kiyomaro learns the importance of friendship and courage. ",
        status = "FINISHED",
        seasonYear = 2003,
        trailer = null
    )
    val animes = listOf(
        anime, anime.copy(id = 2), anime.copy(id = 3)
    )

    Column(modifier = modifier) {
        SectionHeader(title = "Now showing", modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(animes) { item ->
                NowShowingCard(
                    anime = item, onClick = { })
            }
        }
    }
}

@Composable
fun NowShowingCard(anime: Anime, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(143.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier
                .size(width = 143.dp, height = 212.dp)
                .shadow(
                    elevation = 8.dp, shape = RoundedCornerShape(8.dp), clip = false
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray // Placeholder color
            )
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "",
                    tint = GoldStar,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${"%.1f".format(score)}/10 IMDb",
                    style = MaterialTheme.typography.labelMedium,
                    color = LightGray100
                )
            }
        }
    }
}

@Composable
fun PopularSection() {
}

@Composable
fun SectionHeader(modifier: Modifier = Modifier, title: String) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = DarkBlue900)

        OutlinedButton(
            onClick = { },
            modifier = Modifier.height(21.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "See more", style = MaterialTheme.typography.labelSmall, color = LightGray80
            )
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
            ), tonalElevation = 0.dp, containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = true, onClick = { }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_movie_reel),
                    contentDescription = "Home",
                    tint = Color.Unspecified,
                    modifier = modifier.size(24.dp)
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue900, indicatorColor = Color.Transparent
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
//            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                tint = Color.Unspecified,
//                modifier = modifier.size(24.dp)
            )
        }
        Text(
            text = "FilmKu", style = MaterialTheme.typography.titleMedium, color = DarkBlue900
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


