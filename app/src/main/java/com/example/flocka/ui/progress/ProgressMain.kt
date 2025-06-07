package com.example.flocka.ui.progress

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flocka.R
import com.example.flocka.data.model.TodoItem
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.BlueSecondary
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.todo.TodoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgressMain(
    token: String,
    todoViewModel: TodoViewModel = viewModel()
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showEditTaskDialog by remember { mutableStateOf(false) }
    var currentTaskToEdit by remember { mutableStateOf<TodoItem?>(null) }

    val groupedTodos by todoViewModel.groupedTodos.collectAsState()
    val uiErrorMessage by todoViewModel.errorMessage.collectAsState()
    val operationResult by todoViewModel.operationResult.collectAsState()

    var isLoading by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = token) {
        if (token.isNotBlank()) {
            isLoading = true
            Log.d("ProgressMain", "Token available, fetching todos...")
            todoViewModel.fetchTodos(token)
        } else {
            isLoading = false
            Log.w("ProgressMain", "Token is blank in ProgressMain. Cannot fetch.")
        }
    }

    LaunchedEffect(groupedTodos, uiErrorMessage) {
        isLoading = false
    }

    LaunchedEffect(operationResult) {
        operationResult?.let { result ->
            val message = result.fold(
                onSuccess = { it ?: "Operation successful!" },
                onFailure = { "Error: ${it.message ?: "Unknown error"}" }
            )
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
            todoViewModel.clearOperationResult()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDF1F6))
                .padding(scaffoldPadding)
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .then(if (showAddTaskDialog || showEditTaskDialog) Modifier.blur(10.dp) else Modifier),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(26.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.size(26.dp).clickable {
                            if (token.isNotBlank()) {
                                showAddTaskDialog = true
                            } else {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Please log in to add tasks.") }
                            }
                        }
                    )
                }

                if (showAddTaskDialog) {
                    Dialog(
                        onDismissRequest = { showAddTaskDialog = false },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        AddTaskDialog(
                            token = token,
                            onDismiss = { showAddTaskDialog = false }
                        )
                    }
                }

                if (showEditTaskDialog && currentTaskToEdit != null) {
                    Dialog(
                        onDismissRequest = { showEditTaskDialog = false },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        EditTaskDialog(
                            taskToEdit = currentTaskToEdit!!,
                            token = token,
                            onDismiss = { showEditTaskDialog = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                } else if (uiErrorMessage != null) {
                    Text(
                        text = "Error: $uiErrorMessage",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                    )
                } else if (groupedTodos.isEmpty()) {
                    Text(
                        text = "No tasks yet. Add one!",
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        groupedTodos.forEach { (groupTitle, tasksInGroup) ->
                            stickyHeader {
                                Text(
                                    text = groupTitle,
                                    fontFamily = sansationFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (groupTitle == "Past Due") MaterialTheme.colorScheme.error else Color(0xFF808183),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFEDF1F6))
                                        .padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                            items(tasksInGroup, key = { it.todoId }) { todoItem ->
                                TaskCard(
                                    task = todoItem,
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            if (token.isNotBlank()) {
                                                todoViewModel.deleteTodoOnCheck(token, todoItem.todoId)
                                            }
                                        }
                                    },
                                    onEditClick = {
                                        if (token.isNotBlank()) {
                                            currentTaskToEdit = todoItem
                                            showEditTaskDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit
) {
    val todoViewModel: TodoViewModel = viewModel()

    val displayDate = todoViewModel.formatGeneralDisplayDate(task.date, todoViewModel.taskCardDisplayDateFormat)
    val displayStartTime = todoViewModel.formatDisplayTimeFromTimeString(task.startTime)
    val displayEndTime = todoViewModel.formatDisplayTimeFromTimeString(task.endTime)

    var timeText = displayStartTime
    if (displayEndTime.isNotBlank()) {
        timeText += if (timeText.isNotBlank()) " - $displayEndTime" else displayEndTime
    }

    val deadline = if (displayDate.isNotBlank()) "$displayDate, $timeText".trimEnd(',', ' ') else timeText.ifBlank { "No specific time" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onEditClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.fillMaxHeight().width(6.dp).background(BlueSecondary))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = task.taskTitle,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (deadline.isNotBlank() && deadline.lowercase() != "no specific time") {
                            Text(
                                text = deadline,
                                fontFamily = sansationFontFamily,
                                fontSize = 10.sp,
                                color = Color(0xFF808183)
                            )
                        }
                    }
                    if (!task.taskDescription.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = task.taskDescription,
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            color = Color(0xFF808183),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit Task",
                    modifier = Modifier.size(20.dp).clickable { onEditClick() }.padding(start = 4.dp, end = 8.dp),
                    tint = BluePrimary.copy(alpha = 0.6f)
                )
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = BluePrimary,
                        uncheckedColor = BluePrimary.copy(alpha = 0.6f),
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressMainPreview() {
    ProgressMain(token = "preview_token")
}