// quiz/QuizStateManager.kt
package com.example.flocka.ui.home.quiz

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class QuizDialogType {
    PROMPT,          // "QUIZ TIME, Do you want to do your quiz?"
    REMIND_LATER,    // "Remind you later?"
    LOSE_STREAK,     // "NOOO!!! You just lose your streak."
    LETS_START,      // "Let’s Start!!" (timed)
    QUESTION,        // Quiz questions
    SHOWING_ANSWER,  // Intermediate state to show correct/incorrect answers
    CONGRATULATIONS, // "CONGRATULATIONS!! You just finish your quiz."
    NONE             // No dialog visible
}

class QuizStateManager(private val coroutineScope: CoroutineScope) {
    var currentDialog by mutableStateOf(QuizDialogType.NONE)
    var currentQuestionIndex by mutableStateOf(0)
    var selectedAnswerByUser by mutableStateOf<String?>(null) // Store the text of the selected answer
    var isAnswerRevealed by mutableStateOf(false) // To control color change and timed transition

    private var transitionJob: Job? = null // To manage timed transitions

    data class Question(
        val questionText: String,
        val options: List<String>,
        val correctAnswerText: String // Store the correct answer text
    )

    // Updated question to match your example
    val questions = listOf(
        Question(
            "What does UX stands for?",
            listOf("User Xperience", "User Exploration", "User Experience", "Universal Exchange"),
            correctAnswerText = "User Experience"
        )
        // Add more questions if needed
    )

    fun getCurrentQuestion(): Question? {
        return questions.getOrNull(currentQuestionIndex)
    }

    fun getCurrentOptions(): List<String> {
        return getCurrentQuestion()?.options ?: emptyList()
    }

    fun isCorrect(selectedOption: String): Boolean {
        return getCurrentQuestion()?.correctAnswerText == selectedOption
    }

    fun showPrompt() {
        cancelTransition()
        currentDialog = QuizDialogType.PROMPT
        isAnswerRevealed = false
        selectedAnswerByUser = null
    }

    fun handlePromptAnswer(yes: Boolean) {
        cancelTransition()
        if (yes) {
            showLetsStart()
        } else {
            currentDialog = QuizDialogType.REMIND_LATER
        }
    }

    fun handleRemindLaterAnswer(yes: Boolean) {
        cancelTransition()
        if (yes) {
            // Here you would typically schedule a notification or some other reminder logic
            dismissDialog() // For now, just dismiss
        } else {
            currentDialog = QuizDialogType.LOSE_STREAK
        }
    }

    private fun showLetsStart() {
        cancelTransition()
        currentDialog = QuizDialogType.LETS_START
        transitionJob = coroutineScope.launch {
            delay(3000)
            if (currentDialog == QuizDialogType.LETS_START) { // Ensure we are still in this state
                startQuiz()
            }
        }
    }

    fun startQuiz() {
        cancelTransition()
        currentQuestionIndex = 0
        selectedAnswerByUser = null
        isAnswerRevealed = false
        currentDialog = QuizDialogType.QUESTION
    }

    fun selectAnswer(option: String) {
        if (!isAnswerRevealed) { // Allow selection only if answers are not yet revealed
            selectedAnswerByUser = option
            isAnswerRevealed = true // Reveal answers immediately for color change
            // Transition after 3 seconds to the next state
            transitionJob = coroutineScope.launch {
                delay(3000)
                if (currentDialog == QuizDialogType.QUESTION && isAnswerRevealed) {
                    // For a single question quiz, go to congratulations
                    // If multiple questions, you'd go to nextQuestion() or similar
                    if (currentQuestionIndex == questions.lastIndex) {
                        showCongratulations()
                    } else {
                        // Implement nextQuestion logic if needed
                        // nextQuestion()
                        showCongratulations() // Placeholder for now
                    }
                }
            }
        }
    }

    // Call this if you had a "Next Question" button or for multi-question flow
    fun moveToNextPhase() {
        cancelTransition()
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            selectedAnswerByUser = null
            isAnswerRevealed = false
            currentDialog = QuizDialogType.QUESTION
        } else {
            showCongratulations()
        }
    }


    private fun showCongratulations() {
        cancelTransition()
        currentDialog = QuizDialogType.CONGRATULATIONS
    }

    fun dismissDialog() {
        cancelTransition()
        currentDialog = QuizDialogType.NONE
        isAnswerRevealed = false
        selectedAnswerByUser = null
    }

    private fun cancelTransition() {
        transitionJob?.cancel()
        transitionJob = null
    }
}