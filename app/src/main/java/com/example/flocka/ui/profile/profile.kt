package com.example.flocka.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit,
    onMyCommunityClick: () -> Unit,
    onOrderClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSubscriptionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(BluePrimary, shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.img_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Benny Jeans",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansationFontFamily
                )
                Text(
                    "UI / UX Enthusiast",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = sansationFontFamily
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        // Menu List
        val menuItems = listOf(
            Pair("Edit Profile", R.drawable.ic_edit_profile),
            Pair("My Community", R.drawable.ic_my_community),
            Pair("Order", R.drawable.ic_order),
            Pair("Settings", R.drawable.ic_setting),
            Pair("Help", R.drawable.ic_help),
            Pair("Language", R.drawable.ic_language),
            Pair("Subscription", R.drawable.ic_subscription)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            menuItems.forEach { (title, iconRes) ->
                ProfileMenuItemCustom(
                    title = title,
                    iconRes = iconRes,
                    onClick = {
                        when (title) {
                            "Edit Profile" -> onEditProfileClick()
                            "My Community" -> onMyCommunityClick()
                            "Order" -> onOrderClick()
                            "Settings" -> onSettingsClick()
                            "Help" -> onHelpClick()
                            "Language" -> onLanguageClick()
                            "Subscription" -> onSubscriptionClick()
                        }
                    }
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* handle logout */ }
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Logout",
                    tint = BluePrimary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Logout",
                    color = OrangePrimary,
                    fontSize = 17.sp,
                    fontFamily = sansationFontFamily
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItemCustom(title: String, iconRes: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            Modifier.size(24.dp),
            tint = BluePrimary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            fontFamily = sansationFontFamily
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = Color.Black)
    }
}

@Preview
@Composable
fun ProfilePreview(){
    ProfileScreen(
        onEditProfileClick= {},
        onMyCommunityClick= {},
        onOrderClick= {},
        onSettingsClick= {},
        onHelpClick= {},
        onLanguageClick= {},
        onSubscriptionClick = {}
    )
}