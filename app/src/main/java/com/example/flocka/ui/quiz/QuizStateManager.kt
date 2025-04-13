// quiz/QuizStateManager.kt
package com.example.flocka.ui.quiz

import androidx.compose.runtime.*

enum class QuizDialogType {
    PROMPT,          // "Do you want to take the quiz now?"
    DELAY_OPTION,    // "Do it later" vs "Start now"
    LETS_START,      // "Let's Start" message
    QUESTION,        // Quiz questions
    ANSWER,          // Show answer / explanation
    NONE             // No dialog visible
}

class QuizStateManager {
    var currentDialog by mutableStateOf(QuizDialogType.NONE)
    var currentQuestionIndex by mutableStateOf(0)
    var selectedAnswerIndex by mutableStateOf(-1)

    data class Question(
        val questionText: String,
        val options: List<String>,
        val correctAnswer: Int
    )

    val questions = listOf(
        Question("What is 2 + 2?", listOf("3", "4", "5"), correctAnswer = 1),
        Question("What is the capital of France?", listOf("Berlin", "Paris", "Madrid"), correctAnswer = 1),
        // Add more questions
    )

    fun getCurrentQuestion(): Question {
        return questions[currentQuestionIndex]
    }

    fun getCurrentOptions(): List<String> {
        return questions.getOrNull(currentQuestionIndex)?.options ?: emptyList()
    }

    fun showPrompt() {
        currentDialog = QuizDialogType.PROMPT
    }

    fun showDelayOption() {
        currentDialog = QuizDialogType.DELAY_OPTION
    }

    fun showLetsStart() {
        currentDialog = QuizDialogType.LETS_START
    }

    fun startQuiz() {
        currentQuestionIndex = 0
        currentDialog = QuizDialogType.QUESTION
    }

    fun showAnswer() {
        currentDialog = QuizDialogType.ANSWER
    }

    fun nextQuestion() {
        currentQuestionIndex++
        currentDialog = QuizDialogType.QUESTION
        selectedAnswerIndex = -1
    }

    fun dismissDialog() {
        currentDialog = QuizDialogType.NONE
    }
}
