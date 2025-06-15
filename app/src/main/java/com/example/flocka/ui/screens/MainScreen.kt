package com.example.flocka.ui.screens

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.repository.CommunityRepository
import com.example.flocka.data.repository.QuizRepository
import com.example.flocka.navigation.MainNavGraph
import com.yourpackage.ui.components.BottomNavBar
import com.yourpackage.ui.components.TopBar
import com.yourpackage.ui.screens.BaseScreen

@Composable
fun MainScreen(
    token: String,
    communityRepository: CommunityRepository,
    quizRepository: QuizRepository,
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

    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            Log.d("MainScreen", "Token received from NavArg, setting in AuthViewModel: $token")
            authViewModel.setToken(token)
        }
    }

    val showTopBarRoutes = listOf("home", "chat", "progress")
    val showBottomBarRoutes = listOf("home", "chat", "progress", "people", "profile", "space", "event")

    Scaffold(
        topBar = {
            if (currentRoute in showTopBarRoutes) {
                val fullProfileImageUrl = userProfile?.profile_image_url?.let { relativePath ->
                    RetrofitClient.BASE_URL.removeSuffix("/") + relativePath
                }
                TopBar(
                    username = userProfile?.name ?: "Loading...",
                    subtitle = userProfile?.profession ?: "Welcome to Flocka!",
                    profileImageUrl = fullProfileImageUrl,
                    navController = navController
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
            MainNavGraph(
                navController = navController,
                paddingValues = paddingValues,
                token = token,
                communityRepository = communityRepository,
                quizRepository = quizRepository
            )
        }
    }
}
