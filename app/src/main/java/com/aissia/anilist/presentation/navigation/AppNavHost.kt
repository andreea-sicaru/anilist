package com.aissia.anilist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aissia.anilist.presentation.detail.DetailScreen
import com.aissia.anilist.presentation.home.HomeScreen


@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onAnimeClick = { animeId ->
                navController.navigate(Screen.Detail.createRoute(animeId))
            })
        }
        composable(route = Screen.Detail.route, arguments = listOf(navArgument("animeId") {
            type = NavType.IntType
        })) {
            DetailScreen()
        }
    }
}
