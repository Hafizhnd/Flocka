package com.example.flocka.ui.progress

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.yourpackage.ui.components.sansationFontFamily
import java.util.*

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.Bottom)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(495.dp)
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_task),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onDismiss() }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Add New Task",
                        fontFamily = sansationFontFamily,
                        fontSize = 16.sp,
                        color = Color(0xFF172D9D),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
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
                                    .padding(start = 15.dp),
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
                                    .padding(start = 15.dp),
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
                        "Time",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    TimePickerField(context, selectedTime) { selectedTime = it }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Date",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DatePickerField(context, selectedDate) { selectedDate = it }
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .offset(y=46.dp)
                        .height(35.dp)
                        .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp)),
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7F00)),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp) // Remove internal elevation
                    ) {
                        Text(
                            text = "Save Task",
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

@Composable
private fun TimePickerField(context: Context, time: String, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(43.dp)
            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp))
            .padding(start = 15.dp)
            .clickable {
                TimePickerDialog(
                    context,
                    { _, hour, minute -> onTimeSelected(String.format("%02d:%02d", hour, minute)) },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (time.isEmpty()) "Select time" else time,
            fontSize = 13.sp,
            fontFamily = sansationFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFD1D0D0)
        )
    }
}

@Composable
private fun DatePickerField(context: Context, date: String, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(43.dp)
            .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp))
            .padding(start = 15.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        onDateSelected(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (date.isEmpty()) "Select date" else date,
            fontSize = 13.sp,
            fontFamily = sansationFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFD1D0D0)
        )
    }
}

@Preview()
@Composable
fun AddTaskDialogPreview() {
    AddTaskDialog(onDismiss = {})
}