package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class QuizQuestion(
    @SerializedName("quiz_id") val quizId: String,
    @SerializedName("question") val questionText: String,
    @SerializedName("option1") val option1: String,
    @SerializedName("option2") val option2: String,
    @SerializedName("option3") val option3: String?,
    @SerializedName("option4") val option4: String?,
    val correctAnswer: Int
)

data class QuizAnswerRequest(
    @SerializedName("quiz_id") val quizId: String,
    @SerializedName("answer_given") val answerGiven: String
)

data class QuizResultResponseData(
    @SerializedName("quiz_id") val quizId: String,
    @SerializedName("is_correct") val isCorrect: Boolean,
    @SerializedName("correct_answer_text") val correctAnswerText: String?,
    @SerializedName("user_streak") val userStreak: Int
)

typealias QuizResultResponse = GenericApiResponse<QuizResultResponseData>

data class GenericApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String? = null
)