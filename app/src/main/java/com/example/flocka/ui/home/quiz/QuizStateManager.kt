package com.example.flocka.ui.home.quiz

import android.util.Log
import androidx.compose.runtime.*
import com.example.flocka.data.model.QuizQuestion
import com.example.flocka.viewmodel.quiz.QuizViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class QuizDialogType {
    PROMPT,
    REMIND_LATER,
    LOSE_STREAK,
    LETS_START,
    QUESTION,
    SHOWING_ANSWER,
    CONGRATULATIONS,
    NONE
}

class QuizStateManager(
    private val coroutineScope: CoroutineScope,
    val quizViewModel: QuizViewModel,
    private val tokenProvider: () -> String?
) {
    var currentDialog by mutableStateOf(QuizDialogType.NONE)
    var currentQuestion by mutableStateOf<QuizQuestion?>(null)

    var selectedAnswerByUser by mutableStateOf<String?>(null)
    var isAnswerRevealed by mutableStateOf(false)
    var lastQuizResultIsCorrect by mutableStateOf<Boolean?>(null)

    private var transitionJob: Job? = null
    private var questionCollectionJob: Job? = null

    fun getCurrentQuestionFromViewModel(): QuizQuestion? {
        return currentQuestion
    }

    private fun cancelTransition() {
        transitionJob?.cancel()
        transitionJob = null
    }

    private fun clearQuestionStateAndCancelJobs() {
        questionCollectionJob?.cancel()
        questionCollectionJob = null
        currentQuestion = null
    }

    fun showPrompt() {
        cancelTransition()
        clearQuestionStateAndCancelJobs()
        isAnswerRevealed = false
        selectedAnswerByUser = null
        lastQuizResultIsCorrect = null
        currentDialog = QuizDialogType.PROMPT
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
            dismissDialog()
        } else {
            clearQuestionStateAndCancelJobs()
            currentDialog = QuizDialogType.LOSE_STREAK
        }
    }

    private fun showLetsStart() {
        cancelTransition()
        currentDialog = QuizDialogType.LETS_START
        transitionJob = coroutineScope.launch {
            delay(3000)
            if (currentDialog == QuizDialogType.LETS_START) {
                startQuizAttempt()
            }
        }
    }

    fun startQuizAttempt() {
        cancelTransition()
        clearQuestionStateAndCancelJobs()

        selectedAnswerByUser = null
        isAnswerRevealed = false
        lastQuizResultIsCorrect = null
        currentDialog = QuizDialogType.QUESTION

        val tokenValue = tokenProvider()
        Log.d("QuizStateManager", "Attempting to start quiz. Token from provider: ${tokenValue?.take(10)}...")

        if (tokenValue != null && tokenValue.isNotBlank()) {
            Log.d("QuizStateManager", "Token is valid. Fetching quiz question.")
            quizViewModel.fetchQuizQuestion(tokenValue)

            questionCollectionJob = coroutineScope.launch {
                quizViewModel.currentQuestion.collect { fetchedQuestionFromVM ->
                    currentQuestion = fetchedQuestionFromVM
                    Log.d("QuizStateManager", "Collected question from ViewModel: ${fetchedQuestionFromVM?.quizId}")
                }
            }
        } else {
            Log.e("QuizStateManager", "Token is null or blank in startQuizAttempt. Token: '$tokenValue'. Dismissing dialog.")
            dismissDialog()
        }
    }

    fun selectAnswer(option: String) {
        if (!isAnswerRevealed && currentQuestion != null) {
            selectedAnswerByUser = option

            tokenProvider()?.let { token ->
                quizViewModel.submitAnswer(token, currentQuestion!!.quizId, option)
            }

            transitionJob?.cancel()
            transitionJob = coroutineScope.launch {
                quizViewModel.quizResult.filterNotNull().first().let { resultData ->
                    lastQuizResultIsCorrect = resultData.isCorrect
                    isAnswerRevealed = true
                    delay(3000)
                    if (currentDialog == QuizDialogType.QUESTION && isAnswerRevealed) {
                        showCongratulations()
                    }
                }
                quizViewModel.clearQuizResult()
            }
        }
    }

    private fun showCongratulations() {
        cancelTransition()
        currentDialog = QuizDialogType.CONGRATULATIONS
    }

    fun dismissDialog() {
        cancelTransition()
        clearQuestionStateAndCancelJobs()
        currentDialog = QuizDialogType.NONE
        isAnswerRevealed = false
        selectedAnswerByUser = null
        lastQuizResultIsCorrect = null
        quizViewModel.clearQuizResult()
    }
}