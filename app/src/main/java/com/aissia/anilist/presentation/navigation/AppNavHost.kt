package com.aissia.anilist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aissia.anilist.presentation.detail.DetailScreen
import com.aissia.anilist.presentation.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(onAnimeClick = { animeId ->
                navController.navigate(DetailRoute(animeId))
            })
        }
        composable<DetailRoute> {
            DetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
