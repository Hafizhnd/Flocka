package com.yourpackage.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flocka.navigation.MainNavGraph
import com.yourpackage.ui.components.BottomNavBar
import com.yourpackage.ui.components.TopBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Define route groups
    val showTopBarRoutes = listOf("home", "chat", "progress")
    val showBottomBarRoutes = listOf("home", "chat", "progress", "people", "profile", "space", "event")

    Scaffold(
        topBar = {
            if (currentRoute in showTopBarRoutes) {
                TopBar(username = "John Doe", subtitle = "Mobile Developer Enthusiast")
            }
        },
        bottomBar = {
            if (currentRoute in showBottomBarRoutes) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        BaseScreen {
            MainNavGraph(navController = navController, paddingValues = paddingValues)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
