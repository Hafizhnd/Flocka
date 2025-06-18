package com.example.flocka.ui.home.event

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.R
import com.example.flocka.data.model.EventItem
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.repository.EventRepository
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.event.DeleteEventState
import com.example.flocka.viewmodel.event.EventViewModel
import com.example.flocka.viewmodel.todo.TodoViewModel


@Composable
fun EventUI(
    token: String,
    onBackClick: () -> Unit,
    onEventCardClick: (eventId: String) -> Unit,
    eventRepository: EventRepository
) {

    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModel.Factory(eventRepository)
    )

    var showAddEventDialog by remember { mutableStateOf(false) }
    val events by eventViewModel.events.collectAsState()
    val errorMessage by eventViewModel.errorMessage.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = token) {
        if (token.isNotBlank()) {
            isLoading = true
            eventViewModel.fetchEvents(token, "upcoming")
        }
    }

    LaunchedEffect(events, errorMessage) {
        if (events.isNotEmpty() || errorMessage != null) {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEDF1F6))
            .then(if (showAddEventDialog) Modifier.blur(10.dp) else Modifier)
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
                .padding(horizontal = 30.dp)
                .padding(top = 175.dp)
                .shadow(20.dp, shape = RoundedCornerShape(30.dp))
                .background(Color.White, shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(BluePrimary)
                )

                Spacer(modifier = Modifier.width(8.dp))

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
            if (isLoading && events.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                )
            } else if (events.isEmpty()) {
                Text(
                    "No upcoming events found.",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(events, key = { event -> event.eventId }) { eventItem: EventItem ->
                        EventCard(
                            event = eventItem,
                            eventViewModel = eventViewModel,
                            token = token,
                            onClick = {
                                Log.d("EventFlow", "EventCard clicked. Event ID: ${eventItem.eventId}")
                                onEventCardClick(eventItem.eventId)
                            }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddEventDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = BluePrimary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Event",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (showAddEventDialog) {
        Dialog(
            onDismissRequest = { showAddEventDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AddEventDialog(
                token = token,
                onDismiss = { showAddEventDialog = false },
                eventViewModel = eventViewModel
            )
        }
    }
}

@Composable
fun EventCard(
    event: EventItem,
    eventViewModel: EventViewModel,
    token: String,
    onClick: () -> Unit
) {
    val displayDate = eventViewModel.formatEventDisplayDate(event.eventDate)
    val displayStartTime = eventViewModel.formatEventDisplayTime(event.startTime)
    val displayEndTime = eventViewModel.formatEventDisplayTime(event.endTime)
    val deleteEventState by eventViewModel.deleteEventState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

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
        Box {
            Row {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = event.image?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
                        contentDescription = event.name,
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
                            text = "EVENT",
                            fontFamily = sansationFontFamily,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = event.name,
                        fontFamily = sansationFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row {
                        Text(
                            text = "by",
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = event.organizerName ?: event.organizerUsername ?: "Unknown",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = event.location ?: "Location not available",
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
                            text = displayDate.ifBlank { eventViewModel.formatEventDisplayTime(event.eventDate) },
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            Icons.Rounded.Schedule,
                            contentDescription = "Clock",
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp)
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = if (displayEndTime.isNotBlank()) "$displayStartTime-$displayEndTime" else displayStartTime,
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, radius = 12.dp)
                    ) {
                        showDeleteDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                if (deleteEventState is DeleteEventState.Loading &&
                    deleteEventState == DeleteEventState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 1.dp,
                        color = Color.Black
                    )
                } else {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Delete Event",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Delete Event",
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${event.name}\"? This action cannot be undone.",
                    fontFamily = sansationFontFamily
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        eventViewModel.deleteEvent(token, event.eventId)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text(
                        "Delete",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(deleteEventState) {
        when (deleteEventState) {
            is DeleteEventState.Success -> {
                eventViewModel.resetDeleteEventState()
            }
            is DeleteEventState.Error -> {
                val errorState = deleteEventState as DeleteEventState.Error
                Log.e("EventCard", "Delete failed: ${errorState.message}")
                eventViewModel.resetDeleteEventState()
            }
            else -> { /* No action needed */ }
        }
    }
}