package com.example.flocka.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flocka.profile.ui.ProfileScreen
import com.example.flocka.ui.PeoplePage
import com.example.flocka.ui.chat.ChatUIScreen
import com.example.flocka.ui.event_workspace.EventUI
import com.example.flocka.ui.event_workspace.WorkspaceUI
import com.example.flocka.ui.home.HomePage
import com.yourpackage.ui.progress.ProgressMain

@Composable
fun MainNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("home") { HomePage(onSpaceClick = { navController.navigate("workspace") }, onEventClick = { navController.navigate("event") }) }
        composable("workspace") { WorkspaceUI() }
        composable("event") { EventUI() }
        composable("chat") { ChatUIScreen(navController = navController) }
        composable("people") { PeoplePage() }
        composable("progress") { ProgressMain() }
        composable("profile") { ProfileScreen() }
    }
}
