package com.example.flocka

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flocka.ui.onboarding.LandingPage
import com.example.flocka.ui.onboarding.LoginUI
import com.example.flocka.ui.onboarding.RegisterUI
import com.example.flocka.ui.onboarding.SplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "splash"
            ) {
                composable("landing") {
                    LandingPage(
                        onSignUpClick = { navController.navigate("register") },
                        onLoginClick = { navController.navigate("login") }
                    )
                }
                composable("splash") {
                    SplashScreen { navController.navigate("landing") { popUpTo("splash") { inclusive = true } } }
                }
                composable("register") {
                    RegisterUI(
                        onBackClick = { navController.navigate("landing") },
                        onRegisterClick = { email, password ->
                            navController.navigate("login")
                        },
                        onLoginClick = {
                            navController.navigate("login")
                        }
                    )
                }
                composable("login") {
                    LoginUI(
                        onBackClick = { navController.navigate("landing") },
                        onLoginClick = { email, password ->
                            navController.navigate("splash")
                        },
                        onSignUpClick = {
                            navController.navigate("register")
                        }
                    )
                }
            }
        }
    }
}
