package com.yourpackage.ui.progress

import android.R.id.background
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import com.example.flocka.R
import com.example.flocka.ui.progress.AddTaskDialog
import com.yourpackage.ui.components.AppBackground
import com.yourpackage.ui.components.BottomNavBar
import com.yourpackage.ui.components.sansationFontFamily
import com.yourpackage.ui.screens.BaseScreen

@Composable
fun ProgressMain() {
    var showDialog by remember { mutableStateOf(false) }

    var isChecked by remember { mutableStateOf(false) }
    BaseScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .shadow(5.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.White),
                    contentAlignment = Alignment.TopCenter
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(225.dp)
                        ){
                            Text(
                                "Streak",
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                "Keep the good work!!",
                                fontFamily = sansationFontFamily,
                                fontSize = 11.sp,
                                color = Color(0xFF172D9D)
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Background bar with circles inside
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(color = Color(0xFFEDF1F6))
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        (1..7).forEach { day ->
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(10.dp)
                                                        .clip(RoundedCornerShape(50))
                                                        .background(Color.Gray)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 23.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    (1..7).forEach { day ->
                                        Text(
                                            text = day.toString(),
                                            fontSize = 10.sp,
                                            fontFamily = sansationFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }

                        }

                        Box(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_streak),
                                contentDescription = "Background Image",
                                modifier = Modifier
                                    .size(50.dp),
                            )
                            Text(
                                "X",
                                color = Color.White,
                                fontFamily = sansationFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(end = 13.dp, bottom = 9.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                ) {
                    Text(
                        "Your Task",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_add_task),
                        contentDescription = "Add Task",
                        modifier = Modifier
                            .size(26.dp)
                            .clickable { showDialog = true }
                    )

                    if (showDialog) {
                        Dialog(
                            onDismissRequest = { showDialog = false },
                            properties = DialogProperties(usePlatformDefaultWidth = false)
                        ) {
                            AddTaskDialog(onDismiss = { showDialog = false })
                        }
                    }

                }
                Text(
                    "Today",
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF808183)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .height(45.dp)
                        .clip(shape = RoundedCornerShape(5.dp))
                        .background(Color(0xFF00A9F2))
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 24.dp)
                            .background(Color.White),
                        contentAlignment = Alignment.CenterStart
                    ){
                        Row(
                            modifier = Modifier
                                .width(279.dp)
                                .height(27.dp)
                                .offset(x = 12.dp)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(250.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(15.dp)
                                ){
                                    Text(
                                        "Task Name",
                                        fontFamily = sansationFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.widthIn(max = 160.dp)
                                    )

                                    Spacer(modifier = Modifier.width(15.dp))

                                    Text(
                                        "task's deadline",
                                        fontFamily = sansationFontFamily,
                                        fontSize = 10.sp,
                                        color = Color(0xFF808183),
                                        modifier = Modifier
                                            .padding(top = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(1.dp))

                                Text(
                                    "Short Description for the task",
                                    fontFamily = sansationFontFamily,
                                    fontSize = 10.sp,
                                    color = Color(0xFF808183),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 245.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(29.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { isChecked = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF172D9D),
                                        uncheckedColor = Color(0xFF172D9D),
                                        checkmarkColor = Color.White
                                    ),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProgressMainPreview() {
    ProgressMain()
}

