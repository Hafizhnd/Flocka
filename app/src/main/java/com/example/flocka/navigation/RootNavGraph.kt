package com.example.flocka.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flocka.ui.onboarding.*
import com.yourpackage.ui.screens.MainScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("landing") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable("landing") {
            LandingPage(
                onSignUpClick = { navController.navigate("register") },
                onLoginClick = { navController.navigate("login") }
            )
        }
        composable("register") {
            RegisterUI(
                onBackClick = { navController.navigate("landing") },
                onRegisterClick = { _, _ -> navController.navigate("login") },
                onLoginClick = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginUI(
                onBackClick = { navController.navigate("landing") },
                onLoginClick = { _, _ -> navController.navigate("main") },
                onSignUpClick = { navController.navigate("register") }
            )
        }
        composable("main") {
            MainScreen()
        }
    }
}
