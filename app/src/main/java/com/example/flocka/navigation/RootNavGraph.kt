package com.example.flocka.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flocka.data.repository.CommunityRepository
import com.example.flocka.data.repository.SpaceRepository
import com.example.flocka.data.repository.QuizRepository
import com.example.flocka.ui.onboarding.*
import com.example.flocka.ui.screens.MainScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    communityRepository: CommunityRepository,
    quizRepository: QuizRepository,
    spaceRepository: SpaceRepository
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Onboarding Flow
        composable("splash") {
            SplashScreen(
                onNavigateToNextScreen = {
                    navController.navigate("landing") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("landing") {
            LandingPage(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("register") }
            )
        }

        composable("login") {
            LoginUI(
                onLoginSuccess = { user, token ->
                    navController.navigate("main/$token") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("register") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("register") {
            RegisterUI(
                onRegisterSuccess = { user, token ->
                    navController.navigate("setup_account") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate("login") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("setup_account") {
            // Add your SetUpAccountUI here when ready
            // SetUpAccountUI(
            //     onNext = { navController.navigate("add_profile_picture") },
            //     onSkip = { /* Handle skip */ }
            // )
        }

        composable("add_profile_picture") {
            // Add your AddProfilePictureUI here when ready
            // AddProfilePictureUI(
            //     onComplete = { token ->
            //         navController.navigate("main/$token") {
            //             popUpTo("splash") { inclusive = true }
            //         }
            //     }
            // )
        }

        // Main App Flow
        composable(
            route = "main/{token}"
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            MainScreen(
                token = token,
                communityRepository = communityRepository,
                quizRepository = quizRepository,
                spaceRepository = spaceRepository
            )
        }
    }
}
