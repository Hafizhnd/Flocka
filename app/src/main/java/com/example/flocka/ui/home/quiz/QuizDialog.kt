// QuizDialog.kt
package com.example.flocka.ui.home.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.flocka.R // Make sure your R file is imported correctly

val BlueButtonColor = Color(0xFF00A9F2)
val DarkBlueButtonColor = Color(0xFF172D9D)

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
fun QuizRemindLaterDialog( // Renamed for clarity
    onDismiss: () -> Unit,
    onRemind: () -> Unit,
    onNoThanks: () -> Unit // Changed from onCancel to be more specific
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "Remind you later?",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
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
    onClose: () -> Unit // Added a button to close this dialog
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "NOOO!!!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_sad), // Ensure ic_sad exists
            contentDescription = "Sad face",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "You just lose your streak.",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClose, // Use the new onClose lambda
            colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor)
        ) {
            Text("Okay")
        }
    }
}


@Composable
fun QuizLetsStartTimedDialog( // Renamed for clarity
    onDismiss: () -> Unit,
    onTimeout: () -> Unit // Callback for when the 3 seconds are up
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            "Let’s Start!!",
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
fun QuizQuestionDisplayDialog( // Renamed for clarity
    question: QuizStateManager.Question?,
    selectedAnswer: String?,
    isAnswerRevealed: Boolean,
    onAnswerSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (question == null) {
        // Handle case where question might be null, though QuizStateManager should prevent this
        QuizBaseDialog(onDismiss = onDismiss) { Text("Loading question...") }
        return
    }

    QuizBaseDialog(onDismiss = onDismiss) {
        Text(
            question.questionText,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val options = question.options
        if (options.size == 4) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (0..1).forEach { index ->
                        val option = options[index]
                        val buttonColor = if (isAnswerRevealed) {
                            if (option == question.correctAnswerText) Color.Green
                            else if (option == selectedAnswer) Color.Red
                            else BlueButtonColor
                        } else {
                            BlueButtonColor
                        }
                        Button(
                            onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text(option, textAlign = TextAlign.Center)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Or Arrangement.spacedBy(8.dp)
                ) {
                    (2..3).forEach { index ->
                        val option = options[index]
                        val buttonColor = if (isAnswerRevealed) {
                            if (option == question.correctAnswerText) Color.Green
                            else if (option == selectedAnswer) Color.Red
                            else BlueButtonColor
                        } else {
                            BlueButtonColor
                        }
                        Button(
                            onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text(option, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        } else {
            options.forEach { option ->
                val buttonColor = if (isAnswerRevealed) {
                    if (option == question.correctAnswerText) Color.Green
                    else if (option == selectedAnswer) Color.Red
                    else BlueButtonColor
                } else {
                    BlueButtonColor
                }
                Button(
                    onClick = { if (!isAnswerRevealed) onAnswerSelected(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(option)
                }
            }
        }
    }
}


@Composable
fun QuizCongratulationsDialog(
    onDismiss: () -> Unit,
    onClose: () -> Unit
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
            painter = painterResource(id = R.drawable.ic_happy), // Ensure ic_happy exists
            contentDescription = "Happy face",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "You just finished your quiz.",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor)
        ) {
            Text("Awesome!")
        }
    }
}