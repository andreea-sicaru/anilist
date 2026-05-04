package com.aissia.anilist.presentation.home

import androidx.annotation.DrawableRes
import com.aissia.anilist.R

enum class BottomNavItem(@DrawableRes val iconRes: Int, val contentDescription: String) {
    HOME(R.drawable.ic_movie_reel, "Home"),
    TICKETS(R.drawable.ic_ticket, "Tickets"),
    SAVED(R.drawable.ic_saved, "Saved"),
}