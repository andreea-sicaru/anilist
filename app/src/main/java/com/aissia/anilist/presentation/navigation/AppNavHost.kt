package com.aissia.anilist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aissia.anilist.presentation.animelist.AnimeListScreen
import com.aissia.anilist.presentation.detail.DetailScreen
import com.aissia.anilist.presentation.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onAnimeClick = { navController.navigate(DetailRoute(it)) },
                onSeeMore = { navController.navigate(AnimeListRoute(it)) },
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
    }
}
