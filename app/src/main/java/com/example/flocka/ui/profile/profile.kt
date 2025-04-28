package com.example.flocka.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R

// Data class to represent a menu item
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val action: () -> Unit
)

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    val sansationFontFamily = FontFamily(
        androidx.compose.ui.text.font.Font(R.font.sansation_bold, FontWeight.Bold),
        androidx.compose.ui.text.font.Font(R.font.sansation_bold_italic, FontWeight.Bold, FontStyle.Italic),
        androidx.compose.ui.text.font.Font(R.font.sansation_italic, FontWeight.Normal, FontStyle.Italic),
        androidx.compose.ui.text.font.Font(R.font.sansation_light, FontWeight.Light),
        androidx.compose.ui.text.font.Font(R.font.sansation_light_italic, FontWeight.Light, FontStyle.Italic),
        androidx.compose.ui.text.font.Font(R.font.sansation_regular, FontWeight.Normal),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0C258C))
                .padding(top = 24.dp, bottom = 16.dp),
        ) {
            // Back Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    .align(Alignment.TopStart)
                    .clickable { /* Handle back action */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rectangle),
                    contentDescription = "Back Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Back",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = sansationFontFamily
                )
            }

            // Profil info
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.img_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Benny Jeans",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansationFontFamily
                )
                Text(
                    "UI / UX Enthusiast",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = sansationFontFamily
                )
            }
        }

        // Menu List
        val menuItems = listOf(
            MenuItem("Edit Profile", Icons.Default.Edit, {}),
            MenuItem("Settings", Icons.Default.Settings, {}),
            MenuItem("Help", Icons.Default.Help, {}),
            MenuItem("Language", Icons.Default.Language, {}),
            MenuItem("Subscription", Icons.Default.Star, {})
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            menuItems.forEach { item ->
                ProfileMenuItem(
                    title = item.title,
                    icon = item.icon,
                    onClick = item.action,
                    fontFamily = sansationFontFamily
                )
            }

            // Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* handle logout */ }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Logout",
                    color = Color.Red,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector, onClick: () -> Unit, fontFamily: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Color(0xFF0C258C))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = Color.Gray)
    }
}