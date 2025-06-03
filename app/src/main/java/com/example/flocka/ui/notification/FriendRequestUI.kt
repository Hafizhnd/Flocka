package com.example.flocka.ui.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.yourpackage.ui.screens.BaseScreen

data class FriendRequest(
    val id: Int,
    val profile: Int,
    val name: String,
    val username: String
)

@Composable
fun FriendRequestUI(onBackClick: () -> Unit = {}){

    val friendRequests = remember {
        mutableStateListOf(
            FriendRequest(1, R.drawable.p_aline, "Shania Matulessy", "shn.lessy"),
            FriendRequest(2, R.drawable.p_fore, "Jacob Elordi", "jacobelordi"),
            FriendRequest(3, R.drawable.p_allie, "Allie Jordan", "alljo"),
            FriendRequest(4, R.drawable.p_gabriel, "Gabriel Morning Star", "theAngel")
        )
    }

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
                    text = "Friend Request",
                    fontFamily = sansationFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            friendRequests.forEach { friend ->
                FriendRequestList(
                    profile = friend.profile,
                    name = friend.name,
                    username = friend.username,
                    onRemove = {
                        friendRequests.remove(friend)
                    }
                )
            }
        }
    }
}

@Composable
fun FriendRequestList(
    profile: Int,
    name: String,
    username: String,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = profile),
            contentDescription = "Profile picture",
            modifier = Modifier.size(45.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontFamily = alexandriaFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@$username",
                fontFamily = alexandriaFontFamily,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }

        androidx.compose.material3.Button(
            onClick = { onRemove() },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = BluePrimary,
                contentColor = Color.White
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            modifier = Modifier.size(width = 50.dp, height = 20.dp)
        ) {
            Text(
                text = "Confirm",
                fontSize = 10.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        androidx.compose.material3.Button(
            onClick = { onRemove() },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFF808183),
                contentColor = Color.White
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            modifier = Modifier.size(width = 50.dp, height = 20.dp)
        ) {
            Text(
                text = "Delete",
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun FriendRequestPreview(){
    FriendRequestUI()
}
