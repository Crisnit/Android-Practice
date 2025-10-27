package com.example.androidpractice.content
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidpractice.Home

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationRoutes.Home.route) {
        composable(NavigationRoutes.Home.route) {
            Home()
        }
        composable(NavigationRoutes.List.route) {
            ListActivityScreen(navController = navController)
        }
        composable(NavigationRoutes.Filters.route) {
            FilterScreen(navController = navController)
        }
        composable(NavigationRoutes.Favorites.route) {
            FavoritesScreen()
        }
    }
}