package com.yourpackage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.sansationFontFamily


@Composable
fun TopBar(
    username: String = "Your Name",
    subtitle: String = "UI/UX Enthusiast"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(105.dp)
            .background(BluePrimary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 32.dp, top = 45.dp, bottom = 15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_avatar),
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

        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "Notification",
            modifier = Modifier
                .size(20.dp)
                .offset(y = 15.dp, x = (-30).dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    Column(modifier = Modifier.background(BluePrimary)) {
        TopBar(
            username = "John Doe",
            subtitle = "Mobile Developer Enthusiast"
        )
    }
}
