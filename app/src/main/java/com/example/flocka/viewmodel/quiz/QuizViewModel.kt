package com.example.flocka.viewmodel.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.QuizQuestion
import com.example.flocka.data.model.QuizResultResponseData
import com.example.flocka.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _currentQuestion = MutableStateFlow<QuizQuestion?>(null)
    val currentQuestion: StateFlow<QuizQuestion?> = _currentQuestion.asStateFlow()

    private val _quizResult = MutableStateFlow<QuizResultResponseData?>(null)
    val quizResult: StateFlow<QuizResultResponseData?> = _quizResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoadingQuestion = MutableStateFlow(false)
    val isLoadingQuestion: StateFlow<Boolean> = _isLoadingQuestion.asStateFlow()

    private val _isSubmittingAnswer = MutableStateFlow(false)
    val isSubmittingAnswer: StateFlow<Boolean> = _isSubmittingAnswer.asStateFlow()

    private val _refreshProfileTrigger = MutableStateFlow(false)
    val refreshProfileTrigger: StateFlow<Boolean> = _refreshProfileTrigger.asStateFlow()


    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    private val _hasUnsyncedData = MutableStateFlow(false)
    val hasUnsyncedData: StateFlow<Boolean> = _hasUnsyncedData.asStateFlow()

    class Factory(private val quizRepository: QuizRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QuizViewModel(quizRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    init {
        checkUnsyncedData()
    }

    fun fetchQuizQuestion(token: String) {
        viewModelScope.launch {
            _isLoadingQuestion.value = true
            _errorMessage.value = null
            _currentQuestion.value = null // Clear previous question
            _isOfflineMode.value = false


            try {
                quizRepository.getQuizQuestion(token).fold(
                    onSuccess = { question ->
                        _currentQuestion.value = question
                        Log.d("QuizViewModel", "Question fetched successfully: ${question.quizId}")

                        checkUnsyncedData() // Check if sync happened
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to fetch question"
                        _isOfflineMode.value = exception.message?.contains("offline", ignoreCase = true) == true

                        Log.e("QuizViewModel", "Failed to fetch question", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                Log.e("QuizViewModel", "Unexpected error in fetchQuizQuestion", e)
            }

            _isLoadingQuestion.value = false
        }
    }

    fun submitAnswer(token: String, quizId: String, answerGiven: Int) {
        viewModelScope.launch {
            _isSubmittingAnswer.value = true
            _errorMessage.value = null
            _quizResult.value = null


            try {
                quizRepository.submitQuizAnswer(token, quizId, answerGiven).fold(
                    onSuccess = { result ->
                        _quizResult.value = result
                        _refreshProfileTrigger.update { !_refreshProfileTrigger.value }

                        val isOfflineSubmission = result.correctAnswerText?.contains("offline", ignoreCase = true) == true
                        if (isOfflineSubmission) {
                            _isOfflineMode.value = true
                            checkUnsyncedData()
                        }

                        Log.d("QuizViewModel", "Answer submitted successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to submit answer"
                        _isOfflineMode.value = exception.message?.contains("offline", ignoreCase = true) == true

                        Log.e("QuizViewModel", "Failed to submit answer", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                Log.e("QuizViewModel", "Unexpected error in submitAnswer", e)
            }

            _isSubmittingAnswer.value = false
        }
    }

    fun syncUnsyncedData(token: String) {
        viewModelScope.launch {
            try {
                quizRepository.syncAllUnsyncedData(token)
                checkUnsyncedData()
                _refreshProfileTrigger.update { !_refreshProfileTrigger.value }
                Log.d("QuizViewModel", "Unsynced data sync completed")
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Failed to sync unsynced data", e)
            }
        }
    }

    private fun checkUnsyncedData() {
        viewModelScope.launch {
            try {
                _hasUnsyncedData.value = quizRepository.hasUnsyncedData()
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error checking unsynced data", e)
            }
        }
    }

    fun clearQuizResult() {
        _quizResult.value = null
    }

    fun clearOfflineMode() {
        _isOfflineMode.value = false
    }
}