package com.example.flocka.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.flocka.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TicketScreen() {
    var selectedTab by remember { mutableStateOf("Active") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F6FC))
            .padding(16.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF002366)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Order",
                color = Color(0xFF002366),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TabButton(
                    text = "Active Ticket",
                    selected = selectedTab == "Active"
                ) { selectedTab = "Active" }

                Spacer(modifier = Modifier.width(8.dp))

                TabButton(
                    text = "Archived Ticket",
                    selected = selectedTab == "Archived"
                ) { selectedTab = "Archived" }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == "Active") {
            TicketCard(
                title = "Neighborwork",
                location = "Gedung Gondangdia Lama, Jakarta Pusat",
                date = "27 Mar",
                time = "11.00 - 12.30",
                tag = "Space",
                imageRes = R.drawable.rectangel
            )

            Spacer(modifier = Modifier.height(12.dp))

            TicketCard(
                title = "Design Rethink",
                location = "Zoom Meeting",
                date = "20 Nov",
                time = "16.00 - 17.30",
                tag = "Event",
                imageRes = R.drawable.rectangel2
            )
        }
    }
}

@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF002366) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFF002366),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun TicketCard(
    title: String,
    location: String,
    date: String,
    time: String,
    tag: String,
    imageRes: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 105.dp, height = 105.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    tag,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(
                            if (tag == "Space") Color(0xFF4C6EF5) else Color(0xFFFF9900),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )

                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(location, fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(date, fontSize = 12.sp)

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(time, fontSize = 12.sp)
                }
            }
        }
    }
}