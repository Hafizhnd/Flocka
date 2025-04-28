package com.example.flocka.ui.event_workspace

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


@Composable
fun WorkspaceUI() {
    val alexandriaFontFamily = FontFamily(
        androidx.compose.ui.text.font.Font(R.font.alexandria)
    )

    val sansationFontFamily = FontFamily(
        androidx.compose.ui.text.font.Font(R.font.sansation_bold, FontWeight.Bold),
        androidx.compose.ui.text.font.Font(
            R.font.sansation_bold_italic,
            FontWeight.Bold,
            FontStyle.Italic
        ),
        androidx.compose.ui.text.font.Font(
            R.font.sansation_italic,
            FontWeight.Normal,
            FontStyle.Italic
        ),
        androidx.compose.ui.text.font.Font(R.font.sansation_light, FontWeight.Light),
        androidx.compose.ui.text.font.Font(
            R.font.sansation_light_italic,
            FontWeight.Light,
            FontStyle.Italic
        ),
        androidx.compose.ui.text.font.Font(R.font.sansation_regular, FontWeight.Normal),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF172D9D),
                    shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
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
                    // Mengatur tinggi header agar proporsional
                    contentAlignment = Alignment.Center

                ) {
                    // "EVENT" 100% di tengah layar
                    Text(
                        "SPACE",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )

                    // Tombol Back diletakkan di kiri tanpa menggeser teks
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(onClick = { /* Handle back action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_rectangle),
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Deskripsi berada di tengah header
                Text(
                    "Book a coworking space for focused learning and collaboration. Stay productive anytime, anywhere!",
                    fontSize = 12.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.9f) // Membatasi lebar teks agar tidak terlalu panjang
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_search64),
                    contentDescription = "Search",
                    tint = Color(0xFF172D9D),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                    ,
                    // Ukuran ikon
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
                SpaceCard()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SpaceCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Tinggi card disesuaikan
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier) {
            // Gambar persegi penuh di sisi kiri
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cowork3), // Ganti dengan gambar Anda
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
                        .background(Color(0xFF172D9D), shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SPACE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Judul event
                Text(
                    text = "Space Title",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                // Pembicara

                // Lokasi dan waktu
                Text(
                    text = "Gedung BEI Tower 1 Level 3, Unit 304, Daerah Khusus Ibukota Jakarta 12190",
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
                        text = "$1.50/Hours | 9AM-12AM",
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
fun WorkSpaceUIPreview() {
    WorkspaceUI()
}
