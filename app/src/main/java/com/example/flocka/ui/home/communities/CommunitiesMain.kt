package com.example.flocka.ui.home.communities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun CommunitiesMain(
    onBackClick: () -> Unit,
    onCommunityCardClick: () -> Unit,
    // NEW: Callback for when community creation process is fully done
    onCommunityCreationComplete: () -> Unit
) {

    var showNewCommunityDialog by remember { mutableStateOf(false) }
    var showCommunityCreatedDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEDF1F6))
            .then(if (showNewCommunityDialog || showCommunityCreatedDialog) Modifier.blur(10.dp) else Modifier),
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    BluePrimary,
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(top = 72.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "COMMUNITIES",
                            fontSize = 18.sp,
                            fontFamily = alexandriaFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(21.dp),
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_new_community),
                        contentDescription = "New Community",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(27.dp)
                            .clickable { showNewCommunityDialog = true },
                    )

                    // New Community Dialog
                    if (showNewCommunityDialog) {
                        Dialog(
                            onDismissRequest = { showNewCommunityDialog = false },
                            properties = DialogProperties(usePlatformDefaultWidth = false)
                        ) {
                            NewCommunityDialog(
                                onDismiss = { showNewCommunityDialog = false },
                                onCreateCommunity = {
                                    showNewCommunityDialog = false
                                    showCommunityCreatedDialog = true
                                }
                            )
                        }
                    }

                    // Community Created Dialog
                    if (showCommunityCreatedDialog) {
                        Dialog(
                            onDismissRequest = {
                                showCommunityCreatedDialog = false
                                // Only dismiss if the user taps outside,
                                // the 'Done' button handles navigation.
                            },
                            properties = DialogProperties(usePlatformDefaultWidth = false)
                        ) {
                            CommunityCreatedDialog(
                                onDoneClick = {
                                    showCommunityCreatedDialog = false // Dismiss the dialog
                                    onCommunityCreationComplete()      // Navigate to CommunityPage
                                }
                            )
                        }
                    }
                }

                Text(
                    "Find and join communities that match your interests and passions. Connect, share, and grow together!",
                    fontSize = 12.sp,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 207.dp)
        ) {
            CommunityDropdown()
            Spacer(modifier = Modifier.height(16.dp))

            repeat(5) {
                CommunityCard(onClick = onCommunityCardClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CommunityDropdown() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("Trending") }

    val options = listOf("Trending", "Recommended", "Most Recent")

    Column(modifier = Modifier.width(155.dp)) {
        Row(
            modifier = Modifier
                .width(155.dp)
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedOption,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = alexandriaFontFamily,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Rounded.ArrowBackIosNew,
                contentDescription = "Dropdown",
                modifier = Modifier
                    .size(14.dp)
                    .rotate(270f),
                tint = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .width(155.dp)
                .height(1.dp)
                .background(Color.LightGray)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(155.dp)
                .background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            fontFamily = sansationFontFamily
                        )
                    },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CommunityCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.background(Color.White)) {
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .aspectRatio(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.community_img),
                    contentDescription = "Event image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {

                Text(
                    text = "WE LOVE THE EARTH, IT IS OUR PLANET",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "1,200 Members",
                    fontFamily = alexandriaFontFamily,
                    fontSize = 10.sp,
                )

                Spacer(modifier = Modifier.padding(top = 2.dp))

                Text(
                    text = "A chill space for geology enthusiasts who care about the planet. Let’s explore, learn, and protect the Earth together!",
                    fontSize = 8.sp,
                    fontFamily = alexandriaFontFamily,
                    color = Color(0xFF808183),
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunitiesPreview() {
    CommunitiesMain(
        onBackClick = {},
        onCommunityCardClick = {},
        onCommunityCreationComplete = {} // Provide an empty lambda for preview
    )
}