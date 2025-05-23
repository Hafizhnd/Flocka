package com.example.flocka.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.google.accompanist.pager.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.ui.home.quiz.QuizDelayDialog
import com.example.flocka.ui.home.quiz.QuizDialogType
import com.example.flocka.ui.home.quiz.QuizLetsStartDialog
import com.example.flocka.ui.home.quiz.QuizPromptDialog
import com.example.flocka.ui.home.quiz.QuizQuestionDialog
import com.example.flocka.ui.home.quiz.QuizStateManager
import kotlinx.coroutines.delay

@Composable
fun HomePage(
    onSpaceClick: () -> Unit,
    onEventClick: () -> Unit,
    onSeeCommunities: () -> Unit )
{


    val quizManager = remember { QuizStateManager() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp)
            .then(if (quizManager.currentDialog != QuizDialogType.NONE) Modifier.blur(10.dp) else Modifier),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFedf1f7))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AdsSection()

            StreakSection()

            CommunitySection(onSeeCommunities = onSeeCommunities)

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable{onSpaceClick()},
                    painter = painterResource(id = R.drawable.img_space),
                    contentDescription = "space"
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable{onEventClick()},
                    painter = painterResource(id = R.drawable.img_event),
                    contentDescription = "event"
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AdsSection() {
    val adsImages = listOf(
        R.drawable.img_ads_1,
        R.drawable.img_ads_2,
        R.drawable.img_ads_3
    )
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp)
    ) {
        HorizontalPager(
            count = adsImages.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = adsImages[page]),
                contentDescription = "Ad Image"
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(adsImages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) Color.Gray else Color(0xFFD9D9D9))
                )
            }
        }
    }
}

@Composable
fun StreakSection(){

    val quizManager = remember { QuizStateManager() }
    val infiniteTransition = rememberInfiniteTransition()
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            // Wiggle smoothly back and forth for ~1 second
            repeat(3) { // 3 wiggles
                rotation.animateTo(
                    targetValue = -15f,
                    animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                )
                rotation.animateTo(
                    targetValue = 15f,
                    animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                )
            }
            // Return to center
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            )
            delay(1500)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
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
                    color = BluePrimary
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
                    contentDescription = "Wiggle Icon",
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotation.value
                        }
                        .clickable { quizManager.showPrompt() }
                        .size(50.dp)
                )

                when (quizManager.currentDialog) {
                    QuizDialogType.PROMPT -> {
                        Dialog(onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnClickOutside = false,
                                dismissOnBackPress = false
                            )) {
                            QuizPromptDialog(
                                onDismiss = { },
                                onStartQuiz = { quizManager.showLetsStart() },
                                onDelayQuiz = { quizManager.showDelayOption() }
                            )
                        }
                    }

                    QuizDialogType.DELAY_OPTION -> {
                        Dialog(onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnClickOutside = false,
                                dismissOnBackPress = false
                            )) {
                            QuizDelayDialog(
                                onDismiss = { },
                                onRemindLater = { quizManager.showPrompt() },
                                onCancel = { quizManager.dismissDialog() }
                            )
                        }
                    }

                    QuizDialogType.LETS_START -> {
                        Dialog(onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnClickOutside = false,
                                dismissOnBackPress = false
                            )) {
                            QuizLetsStartDialog(
                                onDismiss = { },
                                onBegin = { quizManager.startQuiz() }
                            )
                        }
                    }

                    QuizDialogType.QUESTION -> {
                        Dialog(onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnClickOutside = false,
                                dismissOnBackPress = false
                            )) {
                            QuizQuestionDialog(
                                question = quizManager.getCurrentQuestion().toString(),
                                options = quizManager.getCurrentOptions(),
                                onAnswerSelected = {
                                    quizManager.selectedAnswerIndex = quizManager.getCurrentOptions().indexOf(it)
                                    quizManager.showAnswer()
                                },
                                onDismiss = {  }
                            )
                        }
                    }

                    QuizDialogType.ANSWER -> {
                        Dialog(onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnClickOutside = false,
                                dismissOnBackPress = false
                            )) {
                            QuizAnswerDialog(
                                questionIndex = quizManager.currentQuestionIndex,
                                selectedAnswer = quizManager.selectedAnswerIndex,
                                onNext = { quizManager.nextQuestion() }
                            )
                        }
                    }

                    else -> {

                    }
                }

                Text(
                    "X",
                    color = Color.White,
                    fontFamily = sansationFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 13.dp, bottom = 9.dp)
                        .graphicsLayer {
                            rotationZ = rotation.value
                        }
                )
            }
        }
    }
}

@Composable
fun QuizAnswerDialog(questionIndex: Int, selectedAnswer: Int, onNext: () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun CommunitySection(onSeeCommunities: () -> Unit) {
    val communities = listOf(
        Triple(
            R.drawable.img_community_1,
            "WE LOVE THE EARTH, IT IS OUR PLANET",
            "A chill space for geology enthusiasts who care about the planet. Let’s explore, learn, and protect the Earth together!" to "1,200 Members"
        ),
        Triple(
            R.drawable.img_community_2,
            "Pixel People",
            "A vibrant space for UI/UX and digital design lovers to share ideas, get inspired, and grow together. From pixels to prototypes — let’s create cool stuff!" to "6,312 Members"
        ),
        Triple(
            R.drawable.img_community_3,
            "Digi Marketing MAKES MORE MONEY",
            "A space for digital marketing minds to connect, share strategies, and stay ahead of the trends. From SEO to social — let’s grow together!" to "4,416 Members"
        )
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Communities", style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold))
                Text("Discover your people. Join the conversation!",
                    style = MaterialTheme.typography.labelLarge.copy(color = BluePrimary))
            }
            Text(
                "See All",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = OrangePrimary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { onSeeCommunities() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        communities.forEach { (image, title, descMember) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "community",
                    modifier = Modifier.size(95.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )
                    )
                    Text(
                        descMember.second,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                    )
                    Text(
                        descMember.first,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xffA4A4A6),
                            fontSize = 8.sp,
                            lineHeight = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview(){
    HomePage(
        onEventClick = {},
        onSpaceClick = {},
        onSeeCommunities = {}
    )
}