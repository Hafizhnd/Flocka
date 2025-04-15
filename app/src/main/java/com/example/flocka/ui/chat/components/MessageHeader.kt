package com.example.flocka.ui.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.flocka.ui.components.sansationFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R

@Composable
fun MessageHeader(
    username: String = "Your Name",
    subtitle: String = "UI/UX Enthusiast",
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(105.dp)
            .background(Color(0xFF172D9D))
            .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rectangle),
                    contentDescription = "Back",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(width = 13.dp, height = 10.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = username,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontFamily = sansationFontFamily,
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }

        IconButton(onClick = { /* TODO: Handle action */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "More Options",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageHeaderPreview() {
    Column(modifier = Modifier.background(Color(0xFF172D9D))) {
        MessageHeader(
            username = "Billy Belly",
            subtitle = "UI Designer",
            onBackClick = { }
        )
    }
}
