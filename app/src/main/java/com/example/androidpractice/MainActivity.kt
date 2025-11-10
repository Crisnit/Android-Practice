package com.example.androidpractice
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidpractice.content.BottomNavBar
import com.example.androidpractice.content.NavigationGraph
import com.example.androidpractice.content.NavigationRoutes
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPracticeTheme {
                val navController: NavHostController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route
                val showBottomBar = currentDestination in listOf(
                    NavigationRoutes.Home.route,
                    NavigationRoutes.List.route,
                    NavigationRoutes.Favorites.route,
                    NavigationRoutes.Profile.route
                )
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(routeTranslation(currentDestination.toString()))
                            },
                            colors = TopAppBarColors(Color(0xFF81A681),
                                Color(0xFF81A681),
                                Color(0xFFFFFFFF),
                                Color(0xFFFFFFFF),
                                Color(0xFFFFFFFF))
                        )
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController, state = true)
                        }
                    }) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavigationGraph(navController)
                    }
                }
            }
        }
    }
}
fun routeTranslation(route: String): String {
    when(route){
        "home" -> return "Начальная страница"
        "list" -> return "Список"
        "filters" -> return "Фильтры"
        "favorites" -> return "Избранное"
        "profile" -> return "Профиль"
    }
    return "Неизвестная страница"
}
