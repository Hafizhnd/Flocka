// QuizDialog.kt
package com.example.flocka.ui.home.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.data.model.QuizQuestion

val BlueButtonColor = Color(0xFF00A9F2)
val DarkBlueButtonColor = Color(0xFF172D9D)
val OrangePrimary = Color(0xFFFFA500)

@Composable
fun QuizTimePromptDialog(
    onDismiss: () -> Unit,
    onYes: () -> Unit,
    onNo: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "QUIZ TIME",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Do you want to do your quiz?",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onYes,
                colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor)
            ) {
                Text("Yes")
            }
            Button(
                onClick = onNo,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlueButtonColor)
            ) {
                Text("No")
            }
        }
    }
}

@Composable
fun QuizRemindLaterDialog(
    onDismiss: () -> Unit,
    onRemind: () -> Unit,
    onNoThanks: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "Remind you later?",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = onRemind,
                colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor)
            ) {
                Text("Remind Me")
            }
            Button(
                onClick = onNoThanks,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlueButtonColor)
            ) {
                Text("No Thanks")
            }
        }
    }
}

@Composable
fun QuizLoseStreakDialog(
    onDismiss: () -> Unit,
    onClose: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "NOOO!!!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_sad),
            contentDescription = "Sad face",
            modifier = Modifier.size(75.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "You just lost your streak.",
            textAlign = TextAlign.Center
        )
        Text(
            "Don't worry, you can start a new one tomorrow!",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor)
        ) {
            Text("Okay")
        }
    }
}

@Composable
fun QuizLetsStartTimedDialog(
    onDismiss: () -> Unit,
    onTimeout: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "Let's Start!!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
    // Handled by QuizStateManager now
//    LaunchedEffect(Unit) {
//        delay(3000)
//        onTimeout()
//    }
}

@Composable
fun QuizQuestionDisplayDialog(
    question: QuizQuestion?,
    selectedAnswer: String?,
    isAnswerRevealed: Boolean,
    correctAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (question == null) {
        QuizBaseDialog(onDismiss = onDismiss) { Text("Loading question...") }
        return
    }

    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            question.questionText,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val options = listOfNotNull(
            question.option1,
            question.option2,
            question.option3,
            question.option4
        )

        val buttonModifier = Modifier
            .weight(1f)
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .height(36.dp)

        when (options.size) {
            4 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (0..1).forEach { index ->
                            val option = options[index]
                            val buttonColor = getButtonColor(option, selectedAnswer, correctAnswer, isAnswerRevealed)
                            Button(
                                onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                                modifier = buttonModifier,
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text(option, textAlign = TextAlign.Center, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (2..3).forEach { index ->
                            val option = options[index]
                            val buttonColor = getButtonColor(option, selectedAnswer, correctAnswer, isAnswerRevealed)
                            Button(
                                onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                                modifier = buttonModifier,
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text(option, textAlign = TextAlign.Center, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            3 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (0..1).forEach { index ->
                            val option = options[index]
                            val buttonColor = getButtonColor(option, selectedAnswer, correctAnswer, isAnswerRevealed)
                            Button(
                                onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                                modifier = buttonModifier,
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text(option, textAlign = TextAlign.Center, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val option = options[2]
                        val buttonColor = getButtonColor(option, selectedAnswer, correctAnswer, isAnswerRevealed)
                        Button(
                            onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp, vertical = 2.dp)
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text(option, textAlign = TextAlign.Center, fontSize = 13.sp)
                        }
                    }
                }
            }

            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    options.forEach { option ->
                        val buttonColor = getButtonColor(option, selectedAnswer, correctAnswer, isAnswerRevealed)
                        Button(
                            onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text(option, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCongratulationsDialog(
    onDismiss: () -> Unit,
    onClose: () -> Unit,
    streakCount: Int = 0
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "CONGRATULATIONS!!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00C853), // A green color
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_happy),
            contentDescription = "Happy face",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "You just finished your quiz.",
            textAlign = TextAlign.Center
        )
        if (streakCount > 0) {
            Text(
                "Your streak is now $streakCount days!",
                textAlign = TextAlign.Center,
                color = OrangePrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor)
        ) {
            Text("Awesome!")
        }
    }
}

@Composable
private fun getButtonColor(
    option: String,
    selectedAnswer: String?,
    correctAnswer: String?,
    isAnswerRevealed: Boolean
): Color {
    return if (isAnswerRevealed) {
        when {
            option == correctAnswer -> Color(0xFF00C853)
            option == selectedAnswer -> Color.Red
            else -> BlueButtonColor
        }
    } else {
        BlueButtonColor
    }
}