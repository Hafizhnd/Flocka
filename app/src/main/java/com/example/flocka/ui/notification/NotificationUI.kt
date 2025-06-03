package com.example.flocka.ui.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.sansationFontFamily
import com.yourpackage.ui.screens.BaseScreen

@Composable
fun NotificationUI (onFriendRequestClick: () -> Unit = {}, onBackClick: () -> Unit = {}){

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 27.dp, vertical = 48.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.size(25.dp).clickable(onClick = onBackClick),
                    tint = BluePrimary
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "Notification",
                    fontFamily = sansationFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }
            Spacer(modifier = Modifier.height(28.dp))

            Image(
                painter = painterResource(id = R.drawable.img_friendreq),
                contentDescription = "Friend Request",
                modifier = Modifier
                    .clickable(onClick = onFriendRequestClick)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.img_yesterday),
                contentDescription = "Past Notification",
                modifier = Modifier.padding(end = 36.dp)
            )
        }
    }
}

@Preview
@Composable
fun NotificationUIPreview(){
    NotificationUI()
}