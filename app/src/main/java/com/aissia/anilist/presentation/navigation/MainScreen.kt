package com.aissia.anilist.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.aissia.anilist.presentation.home.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomNavItem.HOME,
                onItemSelected = { item ->
                    when (item) {
                        BottomNavItem.HOME -> navController.navigate(HomeRoute)
                        BottomNavItem.TICKETS -> navController.navigate(TicketsPlaceholderRoute)
                        BottomNavItem.SAVED -> navController.navigate(SavedPlaceholderRoute)
                    }
                }
            )
        }
    ) {
        AppNavHost(navController = navController)
    }
}
