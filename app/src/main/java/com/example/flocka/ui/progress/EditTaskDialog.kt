package com.example.flocka.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flocka.data.model.TodoItem
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.viewmodel.todo.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(
    taskToEdit: TodoItem,
    token: String,
    onDismiss: () -> Unit,
    todoViewModel: TodoViewModel
) {

    var taskTitle by remember { mutableStateOf(taskToEdit.taskTitle) }
    var taskDescription by remember { mutableStateOf(taskToEdit.taskDescription ?: "") }

    fun formatTimeToUi(backendTime: String?): String {
        return backendTime?.takeIf { it.length >= 5 }?.substring(0, 5) ?: ""
    }

    val inputDateFormatFromBackend = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val outputDateFormatForUi = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    fun formatDateToUi(backendDate: String?): String {
        if (backendDate.isNullOrBlank()) return ""
        return try {
            inputDateFormatFromBackend.parse(backendDate)?.let { outputDateFormatForUi.format(it) } ?: ""
        } catch (e: Exception) { "" }
    }

    var selectedStartTime by remember { mutableStateOf(formatTimeToUi(taskToEdit.startTime)) }
    var selectedEndTime by remember { mutableStateOf(formatTimeToUi(taskToEdit.endTime)) }
    var selectedDate by remember { mutableStateOf(formatDateToUi(taskToEdit.date)) }

    var isLoading by remember { mutableStateOf(false) }
    var dialogErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(todoViewModel.operationResult) {
        todoViewModel.operationResult.value?.let { result ->
            isLoading = false
            if (result.isSuccess) {
                onDismiss()
            } else {
                dialogErrorMessage = result.exceptionOrNull()?.message ?: "Failed to update task."
            }
            todoViewModel.clearOperationResult()
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
                .height(530.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp, bottom = 39.dp)
                    .padding(horizontal = 25.dp)
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
                        text = "Edit Task",
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
                        "Task Title",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
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
                                if (taskTitle.isEmpty()) {
                                    Text(
                                        text = "add your task title",
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
                        "Task Description",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
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
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (taskDescription.isEmpty()) {
                                        Text(
                                            text = "add your task description",
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
                    DatePickerField(date = selectedDate) { selectedDate = it }
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
                            if (taskTitle.isBlank()) {
                                dialogErrorMessage = "Task title cannot be empty."
                                return@Button
                            }
                            if (token.isNotBlank()) {
                                isLoading = true
                                todoViewModel.updateTodo(
                                    token = token,
                                    todoId = taskToEdit.todoId,
                                    taskTitle = taskTitle,
                                    taskDescription = taskDescription.ifBlank { null },
                                    startTime = selectedStartTime,
                                    endTime = selectedEndTime,
                                    date = selectedDate
                                )
                                onDismiss()
                            } else {
                                dialogErrorMessage = "Authentication error. Token is missing."
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Save Changes", color = Color.White, fontFamily = sansationFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
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


