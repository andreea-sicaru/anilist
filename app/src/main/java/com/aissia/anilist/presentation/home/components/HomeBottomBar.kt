package com.aissia.anilist.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aissia.anilist.R
import com.aissia.anilist.ui.theme.DarkBlue900
import com.aissia.anilist.ui.theme.Dimens.IconSizeMedium

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
            selected = true,
            onClick = {},
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_movie_reel),
                    contentDescription = "Home",
                    tint = Color.Unspecified,
                    modifier = modifier.size(IconSizeMedium)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue900,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ticket),
                    contentDescription = "Tickets",
                    tint = Color.Unspecified,
                    modifier = modifier.size(IconSizeMedium)
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_saved),
                    contentDescription = "Saved",
                    tint = Color.Unspecified,
                    modifier = modifier.size(IconSizeMedium)
                )
            }
        )
    }
}