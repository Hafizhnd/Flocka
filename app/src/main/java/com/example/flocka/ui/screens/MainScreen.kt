package com.yourpackage.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourpackage.ui.components.BottomNavBar
import com.example.flocka.R
import com.yourpackage.ui.components.TopBar
import com.yourpackage.ui.progress.ProgressMain

@Composable
fun MainScreen() {
    // Set up the navigation controller
    val navController = rememberNavController()

    // Create a simple layout for the MainScreen
    Scaffold(
        topBar = {
            TopBar(
                username = "John Doe",
                subtitle = "Mobile Developer Enthusiast"
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        BaseScreen {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                // Define your different screen destinations
                composable("home") { HomeScreen() }
                composable("chat") { ChatScreen() }
                composable("community") { CommunityScreen() }
                composable("progress") { ProgressMain() }
                composable("profile") { ProfileScreen() }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home Screen")
    }
}

@Composable
fun ChatScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chat Screen")
    }
}

@Composable
fun CommunityScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Community Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Profile Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
