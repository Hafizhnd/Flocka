package com.example.flocka

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.flocka.data.repository.CommunityRepository
import com.example.flocka.data.repository.QuizRepository
import com.example.flocka.di.AppModule
import com.example.flocka.navigation.RootNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val communityRepository = AppModule.provideCommunityRepository(applicationContext)
        val spaceRepository = AppModule.provideSpaceRepository(applicationContext)
        val quizRepository = AppModule.provideQuizRepository(applicationContext)
        val orderRepository = AppModule.provideOrderRepository(applicationContext)
        val todoRepository = AppModule.provideTodoRepository(applicationContext)
        val eventRepository = AppModule.provideEventRepository(applicationContext)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = androidx.navigation.compose.rememberNavController()
            RootNavGraph(
                navController = navController,
                communityRepository = communityRepository,
                todoRepository = todoRepository,
                spaceRepository = spaceRepository,
                quizRepository  = quizRepository,
                orderRepository = orderRepository,
                eventRepository = eventRepository
            )
        }

        hideSystemUI()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.systemBars())
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }
}