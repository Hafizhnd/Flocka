package com.example.flocka.ui.home.communities

import android.R.attr.height
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BorderColor
import androidx.compose.material.icons.rounded.PersonAddAlt
import androidx.compose.material.icons.sharp.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.R
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.community.CommunityViewModel
import com.yourpackage.ui.screens.BaseScreen
import kotlin.math.round
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.flocka.data.repository.CommunityRepository

@Composable
fun CommunityPage(
    communityId: String,
    token: String,
    communityRepository: CommunityRepository,
    onBackClick: () -> Unit,
) {
    val viewModel: CommunityViewModel = viewModel(
        factory = CommunityViewModel.Factory(communityRepository)
    )

    var showNewThreadDialog by remember { mutableStateOf(false) }
    var showEditCommunityDialog by remember { mutableStateOf(false) }

    val selectedCommunity by viewModel.selectedCommunity.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val communityActionResult by viewModel.communityActionResult.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    // Check for network connectivity
    val context = LocalContext.current
    val isOnline = remember { mutableStateOf(isOnline(context)) }

    LaunchedEffect(key1 = communityId, key2 = token) {
        if (token.isNotBlank() && communityId.isNotBlank()) {
            viewModel.fetchCommunityById(token, communityId)
        } else {
            isLoading = false
        }
    }

    // Sync when coming back online
    LaunchedEffect(isOnline.value) {
        if (isOnline.value) {
            viewModel.syncUnsyncedData(token)
        }
    }

    LaunchedEffect(selectedCommunity, errorMessage) {
        isLoading = false
    }

    LaunchedEffect(communityActionResult) {
        communityActionResult?.let { result ->
            if (result.isSuccess) {
            } else {
            }
            viewModel.clearActionResult()
        }
    }

    BaseScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (showNewThreadDialog || showEditCommunityDialog) Modifier.blur(10.dp) else Modifier),

            ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
            } else {
                selectedCommunity?.let { community ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                            .background(BluePrimary)
                            .padding(horizontal = 15.dp)
                            .padding(bottom = 10.dp),
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

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painterResource(id = R.drawable.ic_more),
                            contentDescription = "More",
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp)
                            .padding(top = 101.dp)
                    ) {
                        AsyncImage(
                            model = community.image?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
                            contentDescription = "Community Image",
                            placeholder = painterResource(id = R.drawable.ic_no_image_placeholder),
                            error = painterResource(id = R.drawable.ic_no_image_placeholder),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(70.dp).clip(CircleShape).background(Color(0xFF1E3B75))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                community.name,
                                fontFamily = sansationFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                Icons.Rounded.PersonAddAlt,
                                contentDescription = "Add Member",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(OrangePrimary, shape = CircleShape)
                                    .padding(5.dp)
                            )

                            Spacer(modifier = Modifier.width(5.dp))

                            Button(
                                onClick = {
                                    showEditCommunityDialog = true
                                },
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(30.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BluePrimary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_edit),
                                    contentDescription = "edit",
                                    Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    "Edit Community",
                                    fontSize = 10.sp,
                                    fontFamily = sansationFontFamily
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = community.description ?: "No description available for this community.",
                            fontFamily = alexandriaFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Justify,
                            color = Color(0xFF808183),
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Text(
                                "${community.memberCount ?: 0} Member${if((community.memberCount ?: 0) != 1) "s" else ""}",
                                fontFamily = sansationFontFamily, fontSize = 12.sp,
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Text(
                                "• Public",
                                fontFamily = sansationFontFamily,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(Color.White, shape = RoundedCornerShape(5.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(13.dp))

                            Image(
                                painter = painterResource(R.drawable.img_avatar),
                                contentDescription = "profile image",
                                modifier = Modifier
                                    .size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(9.dp)
                                    .background(Color(0xFFEDF1F6), shape = RoundedCornerShape(5.dp))
                                    .clickable { showNewThreadDialog = true },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    "Write and send your thread",
                                    fontFamily = sansationFontFamily,
                                    fontSize = 10.sp,
                                    color = Color(0xFF808183),
                                    modifier = Modifier.padding(start = 11.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 70.dp,
                                    bottom = 20.dp
                                ), // optional spacing from above
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(R.drawable.ic_no_thread),
                                    contentDescription = "empty thread",
                                    modifier = Modifier
                                        .size(90.dp)
                                )

                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    "No Contents Yet",
                                    fontFamily = sansationFontFamily,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BluePrimary
                                )

                                Spacer(modifier = Modifier.height(7.dp))
                                Text(
                                    "There is no content yet in this community. Write and send your first!",
                                    fontFamily = sansationFontFamily,
                                    fontSize = 12.sp,
                                    color = OrangePrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .width(270.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewThreadDialog) {
        NewThreadDialog(
            onDismiss = { showNewThreadDialog = false }
        )
    }

    // Show the EditCommunityDialog when showEditCommunityDialog is true
    if (showEditCommunityDialog) {
        selectedCommunity?.let { communityToEdit ->
            Dialog(onDismissRequest = { showEditCommunityDialog = false}, properties = DialogProperties(usePlatformDefaultWidth = false) ) {
                EditCommunityDialog(
                    token = token,
                    communityViewModel = viewModel,
                    currentCommunity = communityToEdit,
                    onDismiss = { showEditCommunityDialog = false }
                )
            }
        }
    }
}

// Helper function to check network connectivity (if not already defined)
private fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    )
}