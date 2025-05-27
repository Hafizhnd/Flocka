package com.yourpackage.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flocka.AuthViewModel
import com.example.flocka.navigation.MainNavGraph
import com.yourpackage.ui.components.BottomNavBar
import com.yourpackage.ui.components.TopBar

@Composable
fun MainScreen(
    token: String,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val userProfile by authViewModel.userProfile.collectAsState()

    LaunchedEffect(key1 = token) {
        if (token.isNotBlank()) {
            authViewModel.fetchUserProfile(token)
        }
    }

    // Define route groups
    val showTopBarRoutes = listOf("home", "chat", "progress")
    val showBottomBarRoutes = listOf("home", "chat", "progress", "people", "profile", "space", "event")

    Scaffold(
        topBar = {
            if (currentRoute in showTopBarRoutes) {
                TopBar(
                    username = userProfile?.name ?: "Loading...",
                    subtitle = userProfile?.profession ?: "Welcome to Flocka!"
                )
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
    MainScreen(token = "preview_token")
}
