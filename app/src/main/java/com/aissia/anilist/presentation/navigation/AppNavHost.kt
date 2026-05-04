package com.aissia.anilist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aissia.anilist.presentation.animelist.AnimeListScreen
import com.aissia.anilist.presentation.detail.DetailScreen
import com.aissia.anilist.presentation.home.HomeScreen
import com.aissia.anilist.presentation.home.components.BottomNavItem
import com.aissia.anilist.presentation.placeholder.PlaceholderScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onAnimeClick = { navController.navigate(DetailRoute(it)) },
                onSeeMore = { navController.navigate(AnimeListRoute(it)) },
                onBottomNavClick = { item ->
                    when (item) {
                        BottomNavItem.HOME -> Unit
                        BottomNavItem.TICKETS -> navController.navigate(PlaceholderRoute("Tickets"))
                        BottomNavItem.SAVED -> navController.navigate(PlaceholderRoute("Saved"))
                    }
                },
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
        composable<PlaceholderRoute> { backStackEntry ->
            val route: PlaceholderRoute = backStackEntry.toRoute()
            PlaceholderScreen(
                title = route.title,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
