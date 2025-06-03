package com.example.flocka.ui.event_workspace

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Schedule
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.R
import com.example.flocka.data.model.SpaceItem
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.space.SpaceViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


@Composable
fun WorkspaceUI(
    token: String,
    spaceViewModel: SpaceViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSpaceCardClick: (spaceId: String) -> Unit
) {
    val spaces by spaceViewModel.spaces.collectAsState()
    val errorMessage by spaceViewModel.errorMessage.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = token) {
        if (token.isNotBlank()) {
            spaceViewModel.fetchSpaces(token)
        } else {
            isLoading = false
        }
    }

    LaunchedEffect(spaces, errorMessage) {
        isLoading = false
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F6))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    BluePrimary,
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
                    Text(
                        "SPACE",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = alexandriaFontFamily
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
                .padding(horizontal = 30.dp)
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
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = BluePrimary,
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

        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 240.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp))
            } else if (errorMessage != null) {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else if (spaces.isEmpty()) {
                Text("No spaces found.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(spaces, key = { it.spaceId }) { space ->
                        SpaceCard(
                            space = space,
                            spaceViewModel = spaceViewModel,
                            onClick = { onSpaceCardClick(space.spaceId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpaceCard(
    space: SpaceItem,
    spaceViewModel: SpaceViewModel,
    onClick: () -> Unit
) {
    val openingTimeFormatted = spaceViewModel.formatDisplayTime(space.openingTime)
    val closingTimeFormatted = spaceViewModel.formatDisplayTime(space.closingTime)
    val symbols = remember { DecimalFormatSymbols(Locale.GERMANY) }
    val idrFormat = remember { DecimalFormat("Rp #,##0", symbols) }
    val costFormatted = space.costPerHour?.let { cost ->
        if (cost > 0.0) "${idrFormat.format(cost)}/Hour" else "Free"
    } ?: "N/A"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row{
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
            ) {
                AsyncImage(
                    model = space.image?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
                    contentDescription = space.name,
                    placeholder = painterResource(id = R.drawable.cowork3),
                    error = painterResource(id = R.drawable.cowork3),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(OrangePrimary, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("SPACE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = sansationFontFamily)
                }

                Text(
                    text = space.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansationFontFamily,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = space.location,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = sansationFontFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AttachMoney,
                        contentDescription = "Cost",
                        tint = Color.Gray,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = costFormatted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = sansationFontFamily,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = "Hours",
                        tint = Color.Gray,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = "$openingTimeFormatted - $closingTimeFormatted",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = sansationFontFamily,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WorkSpaceUIPreview() {
    WorkspaceUI(
        token = "preview_token",
        onBackClick = {},
        onSpaceCardClick = {}
    )
}
