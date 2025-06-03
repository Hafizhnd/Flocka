package com.example.flocka.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.foundation.lazy.LazyColumn // Add this import
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flocka.R
import com.example.flocka.viewmodel.OrderViewModel
import coil.compose.AsyncImage
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun TicketScreen(
    token: String,
    onBackClick: () -> Unit,
    orderViewModel: OrderViewModel = viewModel()
) {
    val activeOrders by orderViewModel.activeOrders.collectAsState()
    val archivedOrders by orderViewModel.archivedOrders.collectAsState()
    val errorMessage by orderViewModel.errorMessage.collectAsState()
    var selectedTab by remember { mutableStateOf("Active") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(token) {
        isLoading = true
        orderViewModel.fetchMyOrders(token)
    }

    LaunchedEffect(activeOrders, archivedOrders, errorMessage) {
        isLoading = false
    }

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
                "Order",
                color = Color(0xFF002366),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.width(50.dp))

                TabButton(
                    text = "Expired Ticket",
                    selected = selectedTab == "Expired"
                ) { selectedTab = "Expired" }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val currentOrders = if (selectedTab == "Active") activeOrders else archivedOrders

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage != null) {
            Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
        } else if (currentOrders.isEmpty()) {
            Text("No ${selectedTab.lowercase()} tickets found.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(
                    items = currentOrders,
                    key = { order -> order.orderId }
                ) { order ->
                    TicketCard(
                        title = order.itemName ?: "N/A",
                        location = order.itemType.uppercase(),
                        date = orderViewModel.getDisplayDateForOrder(order.bookedStartDatetime),
                        time = "${orderViewModel.getDisplayTimeForOrder(order.bookedStartDatetime)} - ${orderViewModel.getDisplayTimeForOrder(order.bookedEndDatetime)}",
                        tag = order.itemType.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        imageUrl = order.itemImage,
                        organizer = null,
                        cost = order.amountPaid
                    )
                    Spacer(modifier = Modifier.height(12.dp)) 
                }
            }
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
    imageUrl: String?,
    organizer: String?,
    cost: Double?,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = imageUrl?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
                    contentDescription = title,
                    placeholder = painterResource(id = R.drawable.seminar1), 
                    error = painterResource(id = R.drawable.seminar1),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(OrangePrimary, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tag.uppercase(),
                        fontFamily = sansationFontFamily,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = title,
                    fontFamily = sansationFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!organizer.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "by",
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = organizer,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }


                Text(
                    text = location,
                    fontFamily = sansationFontFamily,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.CalendarToday,
                        contentDescription = "Calendar",
                        tint = Color.Black,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = date,
                        fontFamily = sansationFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Icon(
                        Icons.Rounded.Schedule,
                        contentDescription = "Clock",
                        tint = Color.Black,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = time,
                        fontFamily = sansationFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    if (cost != null && cost > 0) {
                        Icon(
                            Icons.Rounded.MonetizationOn,
                            contentDescription = "Cost",
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(

                            text = "Rp ${try { "%,.0f".format(java.util.Locale("id", "ID"), cost) } catch (e: Exception) { "%.0f".format(cost) }}",
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
