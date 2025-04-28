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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yourpackage.ui.components.BottomNavBar
import com.example.flocka.R
import com.example.flocka.navigation.MainNavGraph
import com.example.flocka.profile.ui.ProfileScreen
import com.example.flocka.ui.PeoplePage
import com.example.flocka.ui.chat.ChatUIScreen
import com.example.flocka.ui.event_workspace.EventUI
import com.example.flocka.ui.event_workspace.WorkspaceUI
import com.example.flocka.ui.home.HomePage
import com.yourpackage.ui.components.TopBar
import com.yourpackage.ui.progress.ProgressMain

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != "people" && currentRoute != "profile") {
                TopBar(username = "John Doe", subtitle = "Mobile Developer Enthusiast")
            }
        },
        bottomBar = { BottomNavBar(navController = navController) }
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
