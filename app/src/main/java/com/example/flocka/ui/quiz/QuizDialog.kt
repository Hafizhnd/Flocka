package com.example.flocka.ui.quiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizPromptDialog(
    onDismiss: () -> Unit,
    onStartQuiz: () -> Unit,
    onDelayQuiz: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text("Do you want to take today's quiz?")
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = onStartQuiz) {
            Text("Start Now")
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Button(onClick = onDelayQuiz) {
            Text("Maybe Later")
        }
    }
}

@Composable
fun QuizDelayDialog(
    onDismiss: () -> Unit,
    onRemindLater: () -> Unit,
    onCancel: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text("Would you like to be reminded later?")
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = onRemindLater) {
            Text("Remind Me")
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Button(onClick = onCancel) {
            Text("Cancel")
        }
    }
}

@Composable
fun QuizLetsStartDialog(
    onDismiss: () -> Unit,
    onBegin: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text("You're all set!")
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = onBegin) {
            Text("Let's Start")
        }
    }
}

@Composable
fun QuizQuestionDialog(
    question: String,
    options: List<String>,
    onAnswerSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    QuizBaseDialog(onDismiss = onDismiss) {
        Text(question)
        Spacer(modifier = Modifier.padding(8.dp))
        options.forEach { option ->
            Button(
                onClick = { onAnswerSelected(option) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(option)
            }
        }
    }
}
