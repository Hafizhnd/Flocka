package com.example.flocka.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.items
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.google.accompanist.flowlayout.FlowRow
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.ui.components.BluePrimary
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun PeoplePage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color(0xFFEDF1F6))
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.splashlogo),
                contentDescription = "Logo Flocka",
                modifier = Modifier
                    .width(105.dp)
                    .height(35.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = "Filter",
                modifier = Modifier.size(20.dp)
            )
        }

        PeopleProfileSwipe(
            imageIds = listOf(
                R.drawable.alexa,
                R.drawable.gavin,
                R.drawable.olivia
            )
        )
    }
}

@Composable
fun PeopleProfileSwipe(
    imageIds: List<Int>
) {
    var currentIndex by remember { mutableStateOf(0) }
    var swipeState by remember { mutableStateOf(PeopleSwipeState.ViewingProfile) }

    val currentImage = imageIds.getOrNull(currentIndex)

    val visible = swipeState == PeopleSwipeState.ViewingProfile
    val matchIcon = when (swipeState) {
        PeopleSwipeState.ShowingMatch -> R.drawable.ic_match
        PeopleSwipeState.ShowingNoMatch -> R.drawable.ic_no_match
        else -> null
    }

    val coroutineScope = rememberCoroutineScope()
    var showHowTo by remember { mutableStateOf(false) }
    var lastSwipedTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(lastSwipedTime) {
        showHowTo = false
        delay(10_000) // 10 seconds
        if (System.currentTimeMillis() - lastSwipedTime >= 10_000) {
            showHowTo = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F6)),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = visible) {
            currentImage?.let { imageId ->
                DraggableProfileCard(
                    imageId = imageId,
                    onSwipedLeft = {
                        lastSwipedTime = System.currentTimeMillis()
                        swipeState = PeopleSwipeState.ShowingNoMatch
                        coroutineScope.launch {
                            delay(1000)
                            moveToNext(imageIds, currentIndex) { newIndex ->
                                currentIndex = newIndex
                                swipeState = PeopleSwipeState.ViewingProfile
                            }
                        }
                    },
                    onSwipedRight = {
                        lastSwipedTime = System.currentTimeMillis()
                        swipeState = PeopleSwipeState.ShowingMatch
                        coroutineScope.launch {
                            delay(1000)
                            moveToNext(imageIds, currentIndex) { newIndex ->
                                currentIndex = newIndex
                                swipeState = PeopleSwipeState.ViewingProfile
                            }
                        }
                    }

                )
            }
        }

        matchIcon?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.size(240.dp)
            )
        }

        if (showHowTo) {
            Image(
                painter = painterResource(id = R.drawable.ic_howto),
                contentDescription = "How To Swipe",
                modifier = Modifier.size(180.dp).offset(y = -100.dp)
            )
        }

    }
}

fun moveToNext(imageIds: List<Int>, currentIndex: Int, onNext: (Int) -> Unit) {
    val next = if (currentIndex + 1 < imageIds.size) currentIndex + 1 else 0
    onNext(next)
}

enum class PeopleSwipeState {
    ViewingProfile,
    ShowingMatch,
    ShowingNoMatch
}


@Composable
fun DraggableProfileCard(
    imageId: Int,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * 0.52f
    val descHeight = screenHeight * 0.48f

    var offsetX by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(targetValue = 1 - (kotlin.math.abs(offsetX) / 1000).coerceIn(0f, 1f))

    Column(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .graphicsLayer(alpha = alpha)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > 300f -> onSwipedRight()
                            offsetX < -300f -> onSwipedLeft()
                            else -> offsetX = 0f
                        }
                    }
                ) { _, dragAmount ->
                    offsetX += dragAmount
                }
            }
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp))
            )

            // Overlay: Name, Age, Pronouns, Interest
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Name, Age", // To be replaced with dynamic data
                    fontFamily = alexandriaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.White
                )
                Text(
                    text = "Pronouns",
                    fontFamily = sansationFontFamily,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "Interest",
                    fontFamily = sansationFontFamily,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        // Bottom: Description section
        Column(
            modifier = Modifier
                .height(descHeight)
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "\"I love snapping photos of anything that catches my eye - from cool city streets to quiet nature spots. Always chasing good vibes, good light, and a good story.”",
                color = Color.Black,
                fontFamily = sansationFontFamily,
                fontSize = 12.5.sp,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "My Interest",
                fontFamily = sansationFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Interest()
        }
    }
}

@Composable
fun Interest() {
    val interests = listOf(
        "Photography", "Traveling", "Tech",
        "Music", "Books", "Nature"
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        interests.take(6).forEach { interest ->
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFB6E9FF), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = interest,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontFamily = sansationFontFamily
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPeoplePage() {
    PeoplePage()
}
