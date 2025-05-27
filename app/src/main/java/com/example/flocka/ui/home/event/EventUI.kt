package com.example.flocka.ui.event_workspace

import android.R.attr.tint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily


@Composable
fun EventUI(
    onBackClick: () -> Unit,
    onEventCardClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEDF1F6))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    BluePrimary,
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(vertical = 40.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        "EVENT",
                        fontSize = 25.sp,
                        fontFamily = alexandriaFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                Text(
                    "Join and book educational events, like seminars and workshops. Learn and grow with the community!",
                    fontSize = 12.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 175.dp)
                .shadow(20.dp, shape = RoundedCornerShape(30.dp))
                .background(Color.White, shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, // Sejajarkan secara vertikal
                horizontalArrangement = Arrangement.Start // Ikon & teks sejajar ke kiri
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(BluePrimary)
                )

                Spacer(modifier = Modifier.width(8.dp)) // Beri jarak antara ikon & teks

                Text(
                    "What are you looking for?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Event List (Placeholder)
        Column(modifier = Modifier.padding(horizontal = 16.dp)
            .padding(top= 240.dp)
        ) {
            repeat(5) {
                EventCard(onClick = onEventCardClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun EventCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.background(Color.White)) {
            Box(
                modifier = Modifier
                    .width(110.dp) // Lebar gambar sama dengan tinggi card
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.seminar1), // Ganti dengan gambar Anda
                    contentDescription = "Event image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // Bagian teks di sebelah kanan
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Badge EVENT
                Box(
                    modifier = Modifier
                        .background(OrangePrimary, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "EVENT",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Judul event
                Text(
                    text = "Event Title",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                // Pembicara
                Text(
                    text = "By Henry Wotton & Jennifer Williams",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )

                // Lokasi dan waktu
                    Text(
                        text = "Jakarta Convention Center",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray

                    )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        ,
                    horizontalArrangement = Arrangement.End // Posisi ke kanan
                ) {
                    Text(
                        text = "17-18 Jan | 11:00-12:30",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End, // Pastikan teks sejajar ke kanan
                        modifier = Modifier.padding(end = 8.dp) // Jarak dari tepi kanan
                    )
                }

            }
        }
    }
}
@Preview
@Composable
fun EventUIPreview() {
    EventUI(
        onBackClick = {},
        onEventCardClick = {})
}
