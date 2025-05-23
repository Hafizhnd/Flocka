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
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate("login") },
                onRegisterSuccess = { _, _ -> navController.navigate("login") }
            )
        }
        composable("login") {
            LoginUI(
                onBackClick = { navController.popBackStack() },
                onSignUpClick = { navController.navigate("register") },
                onLoginSuccess = { _, _ -> navController.navigate("main") }
            )
        }
        composable("main") {
            MainScreen()
        }
    }
}
