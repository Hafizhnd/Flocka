package com.example.flocka.ui.profile.mycommunities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.ui.home.communities.CommunityCard
import com.example.flocka.ui.home.communities.CommunityDropdown

@Composable
fun MyCommunities(
    onBackClick: () -> Unit,
    onCommunityCardClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F6FC))
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF002366)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "My Communities",
                color = Color(0xFF002366),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        repeat(5) {
            CommunityCard(onClick = onCommunityCardClick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun PreviewMyCommunities(){
    MyCommunities (
        onBackClick = {},
        onCommunityCardClick = {}
    )
}