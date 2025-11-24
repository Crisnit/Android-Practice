package com.example.androidpractice.content
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
@Composable
fun BottomNavBar(navController: NavController, state: Boolean, modifier: Modifier = Modifier) {
    NavigationBar(containerColor = Color(0xFF81A681)) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        NavBarItems.BarItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color(0xFF000000),
                    unselectedTextColor = Color(0xFF000000),
                    selectedIconColor = Color(0xFFFFFFFF),
                    selectedTextColor = Color(0xFFFFFFFF)
                )
            )
        }
    }
}
sealed class NavigationRoutes(val route: String) {
    object Home : NavigationRoutes("home")
    object List : NavigationRoutes("list")
    object Filters : NavigationRoutes("filters")
    object Favorites : NavigationRoutes("favorites")
    object Profile : NavigationRoutes("profile")
    object EditProfile : NavigationRoutes("edit_profile")
}
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
object NavBarItems {
    val BarItems = listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        NavigationItem(
            title = "List",
            icon = Icons.AutoMirrored.Filled.List,
            route = "list"
        ),
        NavigationItem(
            title = "Favorites",
            icon = Icons.Filled.Favorite,
            route = "favorites"
        ),
        NavigationItem(
            title = "Профиль",
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}