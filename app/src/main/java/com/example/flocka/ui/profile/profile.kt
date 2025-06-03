package com.example.flocka.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.R
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    token: String,
    onEditProfileClick: () -> Unit,
    onMyCommunityClick: () -> Unit,
    onOrderClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val userProfile by authViewModel.userProfile.collectAsState()

    LaunchedEffect(key1 = token) {
        if (userProfile == null && token.isNotBlank()) {
            authViewModel.fetchUserProfile(token)
        }
    }

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

                val fullProfileImageUrl = userProfile?.profile_image_url?.let { relativePath ->
                    RetrofitClient.BASE_URL.removeSuffix("/") + relativePath
                }

                if (!fullProfileImageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = fullProfileImageUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .background(Color.Gray), // Placeholder color
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.img_avatar),
                        error = painterResource(id = R.drawable.img_avatar)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_avatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(11.dp))
                Text(
                    userProfile?.name ?: "User Name", // Display fetched name
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
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
                    .clickable {
                        authViewModel.logout()
                        onLogoutClick()
                    }
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
        token = "preview_token",
        onEditProfileClick= {},
        onMyCommunityClick= {},
        onOrderClick= {},
        onSettingsClick= {},
        onHelpClick= {},
        onLanguageClick= {},
        onSubscriptionClick = {},
        onLogoutClick = {}
    )
}