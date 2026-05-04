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
import com.aissia.anilist.presentation.theme.DarkBlue900
import com.aissia.anilist.presentation.theme.Dimens.IconSizeMedium
import com.aissia.anilist.presentation.theme.LightGray80

enum class BottomNavItem(val iconRes: Int, val contentDescription: String) {
    HOME(R.drawable.ic_movie_reel, "Home"),
    TICKETS(R.drawable.ic_ticket, "Tickets"),
    SAVED(R.drawable.ic_saved, "Saved"),
}

@Composable
fun HomeBottomBar(
    selectedItem: BottomNavItem = BottomNavItem.HOME,
    onItemSelected: (BottomNavItem) -> Unit = {},
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                clip = false
            ),
        tonalElevation = 0.dp,
        containerColor = Color.White,
    ) {
        BottomNavItem.entries.forEach { item ->
            val selected = item == selectedItem
            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.contentDescription,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(IconSizeMedium),
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
            )
        }
    }
}
