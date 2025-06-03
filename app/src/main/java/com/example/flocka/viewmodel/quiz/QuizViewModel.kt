package com.example.flocka.viewmodel.quiz // Or your ViewModel package

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.QuizAnswerRequest
import com.example.flocka.data.model.QuizQuestion
import com.example.flocka.data.model.QuizResultResponseData
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.viewmodel.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuizViewModel(
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

    fun fetchQuizQuestion(token: String) {
        viewModelScope.launch {
            _isLoadingQuestion.value = true
            _errorMessage.value = null
            _currentQuestion.value = null // Clear previous question
            try {
                val response = RetrofitClient.quizApi.getQuizQuestion("Bearer $token")
                if (response.isSuccessful && response.body()?.success == true) {
                    _currentQuestion.value = response.body()?.data
                } else {
                    _errorMessage.value = response.body()?.message ?: "Failed to fetch question."
                    Log.e("QuizViewModel", "FetchQuestion Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
                Log.e("QuizViewModel", "FetchQuestion Exception", e)
            }
            _isLoadingQuestion.value = false
        }
    }

    fun submitAnswer(token: String, quizId: String, answerGiven: String) {
        viewModelScope.launch {
            _isSubmittingAnswer.value = true
            _errorMessage.value = null
            _quizResult.value = null // Clear previous result
            try {
                val request = QuizAnswerRequest(quizId, answerGiven)
                val response = RetrofitClient.quizApi.submitQuizAnswer("Bearer $token", request)
                if (response.isSuccessful && response.body()?.success == true) {
                    _quizResult.value = response.body()?.data
                    _refreshProfileTrigger.update { !_refreshProfileTrigger.value }
                } else {
                    _errorMessage.value = response.body()?.message ?: "Failed to submit answer."
                    Log.e("QuizViewModel", "SubmitAnswer Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
                Log.e("QuizViewModel", "SubmitAnswer Exception", e)
            }
            _isSubmittingAnswer.value = false
        }
    }

    fun clearQuizResult() {
        _quizResult.value = null
    }
}