package com.example.flocka.ui.home.communities

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Poll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun NewThreadDialog(
    onDismiss: () -> Unit
){
    var isPoll by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            // REMOVED: .padding(horizontal = 20.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 26.dp,) // Internal padding remains
        ){
            Row(
                modifier = Modifier
                    .height(25.dp)
                    .padding(horizontal = 23.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "close",
                    tint = BluePrimary,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { onDismiss() }
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(20.dp)),
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .width(50.dp)
                            .height(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Post",
                            color = Color.White,
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .padding(horizontal = 23.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.img_avatar),
                    contentDescription = "profile pict",
                    modifier = Modifier
                        .size(35.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                ThreadOption(isPoll = isPoll)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Divider()

            Row(
                modifier = Modifier
                    .padding(top = 13.dp)
                    .padding(horizontal = 18.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.ic_text),
                    contentDescription = "type",
                    colorFilter = ColorFilter.tint(if (!isPoll) BluePrimary else Color(0xFF808183)),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { isPoll = false }
                )

                Spacer(modifier = Modifier.width(11.dp))

                Image(
                    painter = painterResource(R.drawable.ic_poll),
                    contentDescription = "poll",
                    colorFilter = ColorFilter.tint(if (isPoll) BluePrimary else Color(0xFF808183)),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { isPoll = true }
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    Icons.Rounded.AttachFile,
                    contentDescription = "attach file",
                    tint = Color(0xFF808183),
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(45f)
                )

                Spacer(modifier = Modifier.width(11.dp))

                Image(
                    painter = painterResource(R.drawable.ic_add_photo),
                    contentDescription = "add photo",
                    modifier = Modifier
                        .size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(11.dp))
        }
    }

}

@Composable
fun ThreadOption(isPoll: Boolean) {
    var thread by remember { mutableStateOf("") }
    var pollOptions by remember { mutableStateOf(mutableListOf("", "")) }
    var showPollDurationDialog by remember { mutableStateOf(false) }
    var pollDurationText by remember { mutableStateOf("1 day") }
    val scrollState = rememberScrollState()
    var pollDays by remember { mutableStateOf(1) }
    var pollHours by remember { mutableStateOf(0) }
    var pollMinutes by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (!isPoll) {
            BasicTextField(
                value = thread,
                onValueChange = { thread = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        if (thread.isBlank()) {
                            Text("Write something...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )
        } else {
            // Fixed height for the whole poll form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp, max = 300.dp) // adjust as needed
                    .border(0.5.dp, Color(0xFF808183), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Text("Polling Name", fontWeight = FontWeight.SemiBold)

                // Make only this scrollable
                Box(
                    modifier = Modifier
                        .weight(1f) // takes all remaining space
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        pollOptions.forEachIndexed { index, option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                BasicTextField(
                                    value = option,
                                    onValueChange = { newValue ->
                                        pollOptions = pollOptions.toMutableList().also {
                                            it[index] = newValue
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(35.dp)
                                        .border(0.5.dp, Color.Gray, RoundedCornerShape(5.dp))
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    decorationBox = { innerTextField ->
                                        if (option.isEmpty()) {
                                            Text("Option ${index + 1}", color = Color.Gray)
                                        }
                                        innerTextField()
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (pollOptions.size < 4 && index == pollOptions.lastIndex) {
                                    Icon(
                                        Icons.Rounded.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable {
                                                pollOptions = pollOptions.toMutableList().apply {
                                                    add("")
                                                }
                                            }
                                    )
                                }
                                if (pollOptions.size > 2) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable {
                                                pollOptions = pollOptions.toMutableList().apply {
                                                    removeAt(index)
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                // Fixed row - always visible
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Poll length: ", fontWeight = FontWeight.Medium)
                    Text(
                        text = pollDurationText,
                        color = BluePrimary,
                        modifier = Modifier.clickable { showPollDurationDialog = true }
                    )
                }
            }
        }
    }

    if (showPollDurationDialog) {
        Dialog(
            onDismissRequest = { showPollDurationDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PollDurationDialog(
                    days = pollDays,
                    hours = pollHours,
                    minutes = pollMinutes,
                    onDismiss = { showPollDurationDialog = false },
                    onDurationSet = { days, hours, minutes ->
                        pollDurationText = buildString {
                            if (days > 0) append("$days day${if (days > 1) "s" else ""} ")
                            if (hours > 0) append("$hours hour${if (hours > 1) "s" else ""} ")
                            if (minutes > 0) append("$minutes minute${if (minutes > 1) "s" else ""}")
                        }.trim()

                        pollDays = days
                        pollHours = hours
                        pollMinutes = minutes
                        showPollDurationDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun PollDurationDialog(
    days: Int,
    hours: Int,
    minutes: Int,
    onDismiss: () -> Unit,
    onDurationSet: (Int, Int, Int) -> Unit
) {
    var d by remember { mutableStateOf(days) }
    var h by remember { mutableStateOf(hours) }
    var m by remember { mutableStateOf(minutes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {},
        text = {
            Column(
                modifier = Modifier
                    .background(Color(0xFFEDF1F6))
                    .padding(16.dp)
            ) {
                Text("Set Length", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LoopingNumberPicker(
                        label = "Days",
                        range = 0..30,
                        selected = days,
                        onSelected = { d = it }
                    )
                    LoopingNumberPicker(
                        label = "Hours",
                        range = 0..23,
                        selected = hours,
                        onSelected = { h = it }
                    )
                    LoopingNumberPicker(
                        label = "Minutes",
                        range = 0..59,
                        selected = minutes,
                        onSelected = { m = it }
                    )
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) { Text("CANCEL") }
                TextButton(onClick = { onDurationSet(d, h, m) }) { Text("SET") }
            }
        },
        containerColor = Color(0xFFEDF1F6)
    )
}


@Composable
fun NumberPicker(
    label: String,
    range: IntRange,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    val itemHeight = 36.dp
    val visibleItemsCount = 3
    val density = LocalDensity.current
    val listState = rememberLazyListState()

    val selectedIndex = (selected - range.first).coerceIn(0, range.count() - 1)
    val centerOffset = visibleItemsCount / 2
    val snapping = rememberSnapFlingBehavior(lazyListState = listState)

    // Scroll to center the selected item on first composition
    LaunchedEffect(Unit) {
        val centeredIndex = (selectedIndex - centerOffset).coerceAtLeast(0)
        listState.scrollToItem(centeredIndex)
    }

    // Determine the centered visible index
    val centeredIndex by remember {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            (first + centerOffset).coerceIn(0, range.count() - 1)
        }
    }

    // Notify selection when scroll settles
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { scrolling ->
                if (!scrolling) {
                    val index = listState.firstVisibleItemIndex + centerOffset
                    val clampedIndex = index.coerceIn(0, range.count() - 1)
                    onSelected(range.first + clampedIndex)
                }
            }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height(itemHeight * visibleItemsCount)
                .width(64.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                flingBehavior = snapping
            ) {
                items(range.count()) { idx ->
                    val value = range.first + idx
                    val isCentered = idx == centeredIndex
                    val color = if (isCentered) Color(0xFF1565C0) else Color.LightGray
                    val fontWeight = if (isCentered) FontWeight.Bold else FontWeight.Normal

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            fontWeight = fontWeight,
                            color = color
                        )
                    }
                }
            }

            // Center dividers
            Divider(
                Modifier
                    .align(Alignment.Center)
                    .offset(y = -itemHeight / 2)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )
            Divider(
                Modifier
                    .align(Alignment.Center)
                    .offset(y = itemHeight / 2)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LoopingNumberPicker(
    label: String,
    range: IntRange,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    val itemHeight = 36.dp
    val visibleItemsCount = 3
    val centerOffset = visibleItemsCount / 2

    val listState = rememberLazyListState()
    val snapping = rememberSnapFlingBehavior(lazyListState = listState)

    val rangeSize = range.count()
    val repeatedCount = Int.MAX_VALUE
    val midPointIndex = repeatedCount / 2 - (repeatedCount / 2) % rangeSize
    val initialIndex = midPointIndex + (selected - range.first)

    // Snap to initial value when the picker opens
    LaunchedEffect(Unit) {
        listState.scrollToItem(initialIndex - centerOffset)
    }

    // Emit new value when scrolling ends
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { scrolling ->
                if (!scrolling) {
                    val index = listState.firstVisibleItemIndex + centerOffset
                    val value = range.first + (index % rangeSize)
                    onSelected(((value - range.first + rangeSize) % rangeSize) + range.first)
                }
            }
    }

    // Derive centered index
    val centeredIndex by remember {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            (first + centerOffset)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height(itemHeight * visibleItemsCount)
                .width(64.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                flingBehavior = snapping
            ) {
                items(repeatedCount) { idx ->
                    val value = range.first + (idx % rangeSize)
                    val isCentered = idx == centeredIndex
                    val color = if (isCentered) Color(0xFF1565C0) else Color.LightGray
                    val fontWeight = if (isCentered) FontWeight.Bold else FontWeight.Normal

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            fontWeight = fontWeight,
                            color = color
                        )
                    }
                }
            }

            Divider(
                Modifier
                    .align(Alignment.Center)
                    .offset(y = -itemHeight / 2)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )
            Divider(
                Modifier
                    .align(Alignment.Center)
                    .offset(y = itemHeight / 2)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}



@Preview
@Composable
fun NewThreadPreview(){
    NewThreadDialog(
        onDismiss = {}
    )
}