package com.yourpackage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItemDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.sansationFontFamily

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val contentDescription: String
) {
    object Home : BottomNavItem("home", R.drawable.ic_home, R.drawable.ic_no_home, "Home")
    object Chat : BottomNavItem("chat", R.drawable.ic_chat, R.drawable.ic_no_chat, "Chat")
    object People : BottomNavItem("people", R.drawable.ic_people, R.drawable.ic_no_people, "People")
    object Progress : BottomNavItem("progress", R.drawable.ic_progress, R.drawable.ic_no_progress, "Progress")
    object Profile : BottomNavItem("profile", R.drawable.ic_profile, R.drawable.ic_no_profile, "Profile")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Chat,
    BottomNavItem.People,
    BottomNavItem.Progress,
    BottomNavItem.Profile
)

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(79.dp)
            .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .background(
                color = Color.White,
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) {
            bottomNavItems.forEach { item ->
                val selected = navBackStackEntry.value?.destination?.route == item.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        val currentRoute = navBackStackEntry.value?.destination?.route
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        val iconSizeModifier = when (item) {
                            is BottomNavItem.Chat -> Modifier.size(width = 32.5.dp, height = 30.dp)
                            is BottomNavItem.Profile -> Modifier.size(width = 35.dp, height = 30.dp)
                            else -> Modifier.size(30.dp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = if (selected) item.selectedIcon else item.unselectedIcon),
                                contentDescription = item.contentDescription,
                                modifier = iconSizeModifier
                            )

                            Text(
                                text = item.contentDescription,
                                color = if (selected) BluePrimary else Color.Gray,
                                fontSize = 10.sp,
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.offset(y = 5.dp)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEDF1F6))
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEDF1F6)
@Composable
fun BottomNavBarPreview() {
    AppBackground {
        BottomNavBar(navController = rememberNavController())
    }
}
