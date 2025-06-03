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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.R
import com.example.flocka.data.model.CommunityItem
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.ui.home.communities.CommunityCard
import com.example.flocka.viewmodel.community.CommunityViewModel

@Composable
fun CommunitiesMain(
    token: String,
    communityViewModel: CommunityViewModel = viewModel(),
    onBackClick: () -> Unit,
    onCommunityClick: (communityId: String) -> Unit,
    onCommunityCreationComplete: (newCommunityId: String) -> Unit
) {

    var showNewCommunityDialog by remember { mutableStateOf(false) }
    var showCommunityCreatedDialog by remember { mutableStateOf(false) }
    var newlyCreatedCommunityId by remember { mutableStateOf<String?>(null) }

    val communities by communityViewModel.communities.collectAsState()
    val myCommunities by communityViewModel.myCommunities.collectAsState()
    val errorMessage by communityViewModel.errorMessage.collectAsState()
    val communityActionResult by communityViewModel.communityActionResult.collectAsState()

    var isLoading by remember { mutableStateOf(true) }
    var selectedFilter by rememberSaveable { mutableStateOf("Trending") }

    LaunchedEffect(token, selectedFilter) {
        if (token.isNotBlank()) {
            isLoading = true
            communityViewModel.fetchCommunities(token, if (selectedFilter == "My Communities") "my" else "all")
        } else {
            isLoading = false
        }
    }

    LaunchedEffect(communities, myCommunities, errorMessage) {
        isLoading = false
    }

    LaunchedEffect(communityActionResult) {
        communityActionResult?.let { result ->
            if (result.isSuccess) {
                val communityItem = result.getOrNull()
                if (showNewCommunityDialog && communityItem != null) {
                    showNewCommunityDialog = false
                    newlyCreatedCommunityId = communityItem.communityId
                    showCommunityCreatedDialog = true
                }
            } else {
            }
            communityViewModel.clearActionResult()
        }
    }

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
                                token = token,
                                communityViewModel = communityViewModel,
                                onDismiss = { showNewCommunityDialog = false },
                                onCreateCommunity = {}
                            )
                        }
                    }

                    if (showCommunityCreatedDialog) {
                        Dialog(
                            onDismissRequest = { showCommunityCreatedDialog = false },
                            properties = DialogProperties(usePlatformDefaultWidth = false)
                        ) {
                            CommunityCreatedDialog(
                                onDoneClick = {
                                    showCommunityCreatedDialog = false
                                    newlyCreatedCommunityId?.let { communityId ->
                                        onCommunityCreationComplete(communityId)
                                    }
                                    newlyCreatedCommunityId = null
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
                .padding(top = 180.dp)
        ) {

            CommunityDropdown(selectedOption = selectedFilter, onOptionSelected = { selectedFilter = it })

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else if (errorMessage != null) {
                Text(
                    "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                )
            } else {
                val communitiesToDisplay = if (selectedFilter == "My Communities") myCommunities else communities
                if (communitiesToDisplay.isEmpty()){
                    Text(
                        "No communities found.",
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(communitiesToDisplay, key = { it.communityId }) { community ->
                            CommunityCard(
                                community = community,
                                onCommunityClick = { onCommunityClick(community.communityId) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CommunityDropdown(selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val options = listOf("Trending", "Recommended", "Most Recent", "My Communities")

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
                modifier = Modifier.size(14.dp).rotate(270f),
                tint = Color.Black
            )
        }
        Box(modifier = Modifier.width(155.dp).height(1.dp).background(Color.LightGray))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(155.dp).background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 14.sp, fontFamily = sansationFontFamily) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun CommunityCard(
    community: CommunityItem,
    onCommunityClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(4.dp)
            .clickable(onClick = onCommunityClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = community.image?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
            contentDescription = community.name,
            placeholder = painterResource(id = R.drawable.img_community_1),
            error = painterResource(id = R.drawable.img_community_1),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(95.dp).clip(RoundedCornerShape(8.dp))
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                community.name, // Dynamic
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${community.memberCount ?: 0} Members", // Dynamic
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
            )
            Text(
                community.description ?: "No description available.", // Dynamic
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xffA4A4A6),
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                ),
                maxLines = 2, // Allow more lines for description
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunitiesPreview() {
    CommunitiesMain(
        token = "preview_token",
        onBackClick = {},
        onCommunityClick = {},
        onCommunityCreationComplete = {}
    )
}