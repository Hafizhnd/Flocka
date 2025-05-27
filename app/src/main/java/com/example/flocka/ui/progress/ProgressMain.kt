package com.yourpackage.ui.progress

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.draw.blur
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.BlueSecondary
import com.example.flocka.ui.progress.AddTaskDialog
import com.example.flocka.ui.components.sansationFontFamily
import com.yourpackage.ui.screens.BaseScreen

@Composable
fun ProgressMain() {
    var showDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }



    BaseScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .then(if (showDialog ) Modifier.blur(10.dp) else Modifier),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
            ){

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

                TaskCard(
                    taskName = "Task Name",
                    deadline = "task's deadline",
                    description = "Short Description for the task",
                    isChecked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
            }
        }
    }

}

@Composable
fun TaskCard(
    taskName: String,
    deadline: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .height(45.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(BlueSecondary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp)
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .width(279.dp)
                    .height(27.dp)
                    .offset(x = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(250.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp)
                    ) {
                        Text(
                            taskName,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 160.dp)
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        Text(
                            deadline,
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            color = Color(0xFF808183),
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        description,
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
                        onCheckedChange = onCheckedChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = BluePrimary,
                            uncheckedColor = BluePrimary,
                            checkmarkColor = Color.White
                        ),
                        modifier = Modifier.size(15.dp)
                    )
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

