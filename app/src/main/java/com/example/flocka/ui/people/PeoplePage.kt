package com.example.flocka.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.flocka.R


@Composable
fun PeoplePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .width(390.dp)
                .height(75.dp)
                .background(Color(0xEDF1F6))
        ) {
            Image(
                painter = painterResource(id = R.drawable.splashlogo),
                contentDescription = "Logo Flocka",
                modifier = Modifier
                    .padding(
                        start = 30.dp,
                        top = 38.dp
                    )
                    .width(105.dp)
                    .height(35.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = "Filter",
                modifier = Modifier
                    .padding(
                        start = 342.dp,
                        top = 45.dp
                    )
                    .height(20.dp)
                    .width(20.dp)
            )
        }
        // 🔥 Tambahkan pemanggilan di sini
        ScrollableSingleImage()
    }
}

@Composable
fun ScrollableSingleImage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEdf1f6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Gambar pertama
        Image(
            painter = painterResource(id = R.drawable.alexa),
            contentDescription = "Gambar Alexa",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(360.dp)
                .height(610.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Gambar kedua
        Image(
            painter = painterResource(id = R.drawable.gavin),
            contentDescription = "Gambar Gavin",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(360.dp)
                .height(610.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Gambar ketiga
        Image(
            painter = painterResource(id = R.drawable.olivia),
            contentDescription = "Gambar Olivia",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(360.dp)
                .height(610.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    PeoplePage()
}
