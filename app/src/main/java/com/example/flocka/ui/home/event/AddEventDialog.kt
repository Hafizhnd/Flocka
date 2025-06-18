package com.example.flocka.ui.home.event

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.event.CreateEventState
import com.example.flocka.viewmodel.event.EventViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(
    token: String,
    onDismiss: () -> Unit,
    eventViewModel: EventViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedStartTime by remember { mutableStateOf("") }
    var selectedEndTime by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var ticketPrice by remember { mutableStateOf("") }
    val cost = ticketPrice.toDoubleOrNull() ?: 0.00
    var location by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val createEventState by eventViewModel.createEventState.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    var dialogErrorMessage by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        }
    }

    LaunchedEffect(createEventState) {
        when (createEventState) {
            is CreateEventState.Loading -> {
                isLoading = true
                dialogErrorMessage = null
            }
            is CreateEventState.Success -> {
                isLoading = false
                eventViewModel.resetCreateEventState()
                onDismiss()
            }
            is CreateEventState.Error -> {
                isLoading = false
                val errorState = createEventState as CreateEventState.Error
                dialogErrorMessage = errorState.message
            }
            is CreateEventState.Idle -> {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.Bottom)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp, bottom = 39.dp)
                    .padding(horizontal = 25.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Back",
                        tint = BluePrimary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Add New Event",
                        fontFamily = sansationFontFamily,
                        fontSize = 16.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column (
                    modifier = Modifier
                        .padding(horizontal = 15.dp),
                ){
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        "Event Title",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(43.dp)
                            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp)),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 15.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (name.isEmpty()) {
                                    Text(
                                        text = "add your event title",
                                        fontSize = 13.sp,
                                        fontFamily = sansationFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFFD1D0D0)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Event Description",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        singleLine = false,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp)
                            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp)),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.fillMaxSize().padding(15.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (description.isEmpty()) {
                                        Text(
                                            text = "add your event description",
                                            fontSize = 13.sp,
                                            fontFamily = sansationFontFamily,
                                            color = Color(0xFFD1D0D0)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Event Image",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .border(
                                1.dp,
                                Color(0xFFD1D0D0),
                                RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(15.dp))
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            }
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected event image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Change image",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        "Change Image",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontFamily = sansationFontFamily
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add image",
                                    tint = Color(0xFFD1D0D0),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Add Event Image",
                                    color = Color(0xFFD1D0D0),
                                    fontSize = 13.sp,
                                    fontFamily = sansationFontFamily
                                )
                                Text(
                                    "Tap to select from gallery",
                                    color = Color(0xFFD1D0D0),
                                    fontSize = 11.sp,
                                    fontFamily = sansationFontFamily
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Event Ticket Price",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicTextField(
                        value = ticketPrice,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                ticketPrice = newValue
                            }
                        },
                        singleLine = false,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp)
                            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp)),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.fillMaxSize().padding(15.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (ticketPrice.isEmpty()) {
                                        Text(
                                            text = "add your event ticket price",
                                            fontSize = 13.sp,
                                            fontFamily = sansationFontFamily,
                                            color = Color(0xFFD1D0D0)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Event location",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicTextField(
                        value = location,
                        onValueChange = { location = it },
                        singleLine = false,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp)
                            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp)),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.fillMaxSize().padding(15.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (location.isEmpty()) {
                                        Text(
                                            text = "add your event location",
                                            fontSize = 13.sp,
                                            fontFamily = sansationFontFamily,
                                            color = Color(0xFFD1D0D0)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Start Time",
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            TimePickerField(label = "Select Start Time", time = selectedStartTime) { selectedStartTime = it }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "End Time",
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            TimePickerField(label = "Select End Time", time = selectedEndTime) { selectedEndTime = it }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Date",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DatePickerField(date = eventDate) { eventDate = it }
                }

                Spacer(modifier = Modifier.height(42.dp))

                Box(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .height(35.dp)
                        .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp)),
                ) {
                    Button(
                        onClick = {
                            Log.d("AddEventDebug", "Save Event button clicked.")

                            when {
                                name.isBlank() -> {
                                    dialogErrorMessage = "Event title cannot be empty."
                                    return@Button
                                }
                                location.isBlank() -> {
                                    dialogErrorMessage = "Event location cannot be empty."
                                    return@Button
                                }
                                eventDate.isBlank() -> {
                                    dialogErrorMessage = "Event date must be selected."
                                    return@Button
                                }
                                selectedStartTime.isBlank() -> {
                                    dialogErrorMessage = "Start time must be selected."
                                    return@Button
                                }
                                selectedEndTime.isBlank() -> {
                                    dialogErrorMessage = "End time must be selected."
                                    return@Button
                                }
                                token.isBlank() -> {
                                    dialogErrorMessage = "Authentication error. Token is missing."
                                    return@Button
                                }
                            }

                            val convertedDate = try {
                                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val date = inputFormat.parse(eventDate)
                                if (date != null) outputFormat.format(date) else eventDate
                            } catch (e: Exception) {
                                Log.e("AddEventDebug", "Date conversion error", e)
                                eventDate
                            }

                            Log.d("AddEventDebug", "Creating event with data:")
                            Log.d("AddEventDebug", "Name: $name")
                            Log.d("AddEventDebug", "Description: $description")
                            Log.d("AddEventDebug", "Date: $convertedDate")
                            Log.d("AddEventDebug", "Start Time: $selectedStartTime")
                            Log.d("AddEventDebug", "End Time: $selectedEndTime")
                            Log.d("AddEventDebug", "Location: $location")
                            Log.d("AddEventDebug", "Cost: $cost")

                            eventViewModel.createEventWithStringDatesAndImage(
                                token = token,
                                name = name,
                                description = if (description.isBlank()) null else description,
                                eventDateString = convertedDate,
                                startTimeString = selectedStartTime,
                                endTimeString = selectedEndTime,
                                location = location,
                                imageUri = selectedImageUri,
                                cost = if (cost > 0) cost else null
                            )
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Save Event",
                                color = Color.White,
                                fontFamily = sansationFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
    dialogErrorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { dialogErrorMessage = null },
            title = { Text("Error") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { dialogErrorMessage = null }) { Text("OK") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerField(
    label: String,
    time: String,
    onTimeSelected: (String) -> Unit
) {
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = if (time.isNotBlank()) time.substringBefore(":").toIntOrNull() ?: 0 else Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = if (time.isNotBlank()) time.substringAfter(":").toIntOrNull() ?: 0 else Calendar.getInstance().get(Calendar.MINUTE),
        is24Hour = true
    )

    if (showTimePickerDialog) {
        AlertDialog(
            onDismissRequest = { showTimePickerDialog = false },
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color(0xFFEDF1F6),
            title = { Text(label, color = BluePrimary) },
            text = {
                TimePicker(
                    state = timePickerState,
                    layoutType = TimePickerLayoutType.Vertical,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = BluePrimary.copy(alpha = 0.1f),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = BluePrimary.copy(alpha = 0.7f),
                        selectorColor = OrangePrimary,
                        containerColor = Color(0xFFEDF1F6),
                        periodSelectorBorderColor = BluePrimary,
                        periodSelectorSelectedContainerColor = BluePrimary,
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = BluePrimary,
                        timeSelectorSelectedContainerColor = BluePrimary,
                        timeSelectorUnselectedContainerColor = BluePrimary.copy(alpha = 0.2f),
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = BluePrimary
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelected(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
                    showTimePickerDialog = false
                }) {
                    Text("OK", color = OrangePrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text("Cancel", color = OrangePrimary)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(43.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp))
            .padding(horizontal = 15.dp)
            .clickable { showTimePickerDialog = true },
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (time.isEmpty()) "Select time" else time,
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.Normal,
                color = if (time.isEmpty()) Color(0xFFD1D0D0).copy(alpha = 0.7f) else Color.Black
            )
            Icon(
                imageVector = Icons.Rounded.AccessTime,
                contentDescription = "$label Icon",
                tint = Color(0xFFD1D0D0),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(date: String, onDateSelected: (String) -> Unit) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(dateFormatter.format(Date(millis)))
                    }
                    showDatePickerDialog = false
                }) { Text("OK", color = OrangePrimary) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancel", color = OrangePrimary) }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFFEDF1F6),
                titleContentColor = BluePrimary,
                headlineContentColor = BluePrimary,
                weekdayContentColor = BluePrimary.copy(alpha = 0.7f),
                subheadContentColor = BluePrimary,
                yearContentColor = BluePrimary,
                currentYearContentColor = BluePrimary,
                selectedYearContainerColor = BluePrimary,
                selectedYearContentColor = Color.White,
                dayContentColor = BluePrimary,
                disabledDayContentColor = BluePrimary.copy(alpha = 0.3f),
                selectedDayContainerColor = BluePrimary,
                selectedDayContentColor = Color.White,
                disabledSelectedDayContainerColor = BluePrimary.copy(alpha = 0.2f),
                disabledSelectedDayContentColor = BluePrimary.copy(alpha = 0.5f),
                todayDateBorderColor = OrangePrimary,
                todayContentColor = OrangePrimary,
                dayInSelectionRangeContainerColor = BluePrimary.copy(alpha = 0.2f),
                dayInSelectionRangeContentColor = BluePrimary
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(43.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp))
            .padding(horizontal = 15.dp)
            .clickable { showDatePickerDialog = true },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = if (date.isEmpty()) "Select date" else date,
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.Normal,
                color = if (date.isEmpty()) Color(0xFFD1D0D0).copy(alpha = 0.7f) else Color.Black
            )
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Date Icon",
                tint = Color(0xFFD1D0D0),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}