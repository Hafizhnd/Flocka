package com.example.flocka.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flocka.ui.onboarding.*
import com.yourpackage.ui.screens.MainScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                onRegisterSuccess = { user, token ->
                    val encodedUsername = URLEncoder.encode(user.username, StandardCharsets.UTF_8.toString())
                    navController.navigate("setupaccount/$encodedUsername/$token")
                }
            )
        }

        composable(
            route = "setupaccount/{username}/{token}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val token = backStackEntry.arguments?.getString("token") ?: ""

            SetUpAccountUI(
                username = username,
                token = token,
                onDoneClick = {
                    navController.navigate("addprofilepicture/$token") {
                        popUpTo("setupaccount/{username}/{token}") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "addprofilepicture/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            AddProfilePictureUI(
                token = token,
                onSaveSuccess = {
                    navController.navigate("main/$token") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginUI(
                onBackClick = { navController.popBackStack() },
                onSignUpClick = { navController.navigate("register") },
                onLoginSuccess = { user, token ->
                    navController.navigate("main/$token") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            )
        }
        composable("main/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            MainScreen(token = token)
        }
    }
}
