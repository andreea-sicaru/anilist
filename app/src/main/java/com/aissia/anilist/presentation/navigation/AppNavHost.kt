package com.aissia.anilist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aissia.anilist.presentation.animelist.AnimeListScreen
import com.aissia.anilist.presentation.detail.DetailScreen
import com.aissia.anilist.presentation.home.HomeScreen
import com.aissia.anilist.presentation.placeholder.SavedPlaceholderScreen
import com.aissia.anilist.presentation.placeholder.TicketsPlaceholderScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    bottomBarHeight: Dp = 0.dp,
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onAnimeClick = { navController.navigate(DetailRoute(it)) },
                onSeeMore = { navController.navigate(AnimeListRoute(it)) },
                bottomBarHeight = bottomBarHeight,
            )
        }
        composable<DetailRoute> {
            DetailScreen(onBack = { navController.popBackStack() })
        }
        composable<AnimeListRoute> {
            AnimeListScreen(
                onBack = { navController.popBackStack() },
                onAnimeClick = { navController.navigate(DetailRoute(it)) },
            )
        }
        composable<TicketsPlaceholderRoute> {
            TicketsPlaceholderScreen(
                onBack = { navController.navigate(HomeRoute) },
            )
        }
        composable<SavedPlaceholderRoute> {
            SavedPlaceholderScreen(
                onBack = { navController.navigate(HomeRoute) },
            )
        }
    }
}
