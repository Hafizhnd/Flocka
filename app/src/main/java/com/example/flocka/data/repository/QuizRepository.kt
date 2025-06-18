package com.example.flocka.data.repository

import android.util.Log
import com.example.flocka.data.local.dao.QuizDao
import com.example.flocka.data.local.entity.QuizEntity
import com.example.flocka.data.local.entity.QuizResultEntity
import com.example.flocka.data.model.QuizAnswerRequest
import com.example.flocka.data.model.QuizQuestion
import com.example.flocka.data.model.QuizResultResponseData
import com.example.flocka.data.remote.QuizApi
import java.io.IOException

class QuizRepository(
    private val quizApi: QuizApi,
    private val quizDao: QuizDao
) {

    suspend fun getQuizQuestion(token: String): Result<QuizQuestion> {
        return try {
            syncUnsyncedData(token)

            val response = quizApi.getQuizQuestion("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val question = response.body()?.data
                if (question != null) {
                    quizDao.insertQuizQuestion(QuizEntity.fromQuizQuestion(question, isSynced = true))
                    Result.success(question)
                } else {
                    getCachedQuestionOrError()
                }
            } else {
                getCachedQuestionOrError()
            }
        } catch (e: IOException) {
            Log.d("QuizRepository", "Network error - using any cached question for offline practice")
            getCachedQuestionOrError()
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error fetching quiz question", e)
            getCachedQuestionOrError()
        }
    }

    private suspend fun getCachedQuestionOrError(): Result<QuizQuestion> {
        val cachedQuestion = quizDao.getRandomQuizQuestion()

        return if (cachedQuestion != null) {
            Log.d("QuizRepository", "Using cached question for offline practice: ${cachedQuestion.quizId}")
            Result.success(cachedQuestion.toQuizQuestion())
        } else {
            Result.failure(Exception("No questions available offline"))
        }
    }

    suspend fun submitQuizAnswer(
        token: String,
        quizId: String,
        answerGiven: Int
    ): Result<QuizResultResponseData> {
        return try {
            val request = QuizAnswerRequest(quizId, answerGiven.toString())
            val response = quizApi.submitQuizAnswer("Bearer $token", request)

            if (response.isSuccessful && response.body()?.success == true) {
                val result = response.body()?.data
                if (result != null) {
                    quizDao.markQuizAsUsed(quizId)

                    quizDao.insertQuizResult(
                        QuizResultEntity.fromQuizResultResponseData(
                            result,
                            answerGiven,
                            isSynced = true
                        )
                    )

                    Result.success(result)
                } else {
                    storeOfflineAnswer(quizId, answerGiven)
                    val offlineResult = createOfflineResult(quizId, answerGiven)
                    Result.success(offlineResult)
                }
            } else {
                storeOfflineAnswer(quizId, answerGiven)
                val offlineResult = createOfflineResult(quizId, answerGiven)
                Result.success(offlineResult)
            }
        } catch (e: IOException) {
            Log.d("QuizRepository", "Offline - storing answer for later sync and checking locally")
            storeOfflineAnswer(quizId, answerGiven)

            val offlineResult = createOfflineResult(quizId, answerGiven)
            Result.success(offlineResult)
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error submitting quiz answer", e)
            storeOfflineAnswer(quizId, answerGiven)
            Result.failure(e)
        }
    }

    private suspend fun storeOfflineAnswer(quizId: String, answerGiven: Int) {
        val correctAnswer = quizDao.getCorrectAnswerForQuiz(quizId)
        val isCorrect = correctAnswer != null && correctAnswer == answerGiven

        val quiz = quizDao.getQuizById(quizId)

        val correctAnswerText = when (correctAnswer) {
            1 -> quiz?.option1
            2 -> quiz?.option2
            3 -> quiz?.option3
            4 -> quiz?.option4
            else -> "Unknown"
        } ?: "Unknown"

        val offlineResult = QuizResultEntity(
            quizId = quizId,
            isCorrect = isCorrect,
            correctAnswerText = correctAnswerText,
            userStreak = 0,
            answerGiven = answerGiven,
            isSynced = false
        )
        quizDao.insertQuizResult(offlineResult)
        Log.d("QuizRepository", "Answer stored offline for quiz: $quizId (correct: $isCorrect, correct answer: $correctAnswerText)")
    }

    private suspend fun createOfflineResult(quizId: String, answerGiven: Int): QuizResultResponseData {
        val correctAnswer = quizDao.getCorrectAnswerForQuiz(quizId)
        val isCorrect = correctAnswer != null && correctAnswer == answerGiven

        val quiz = quizDao.getQuizById(quizId)
        val correctAnswerText = when (correctAnswer) {
            1 -> quiz?.option1
            2 -> quiz?.option2
            3 -> quiz?.option3
            4 -> quiz?.option4
            else -> null
        }

        return QuizResultResponseData(
            quizId = quizId,
            isCorrect = isCorrect,
            correctAnswerText = correctAnswerText,
            userStreak = 0
        )
    }

    private suspend fun syncUnsyncedData(token: String) {
        try {
            val unsyncedResults = quizDao.getUnsyncedQuizResults()
            unsyncedResults.forEach { result ->
                try {
                    val request = QuizAnswerRequest(result.quizId, result.answerGiven.toString())
                    val response = quizApi.submitQuizAnswer("Bearer $token", request)

                    if (response.isSuccessful && response.body()?.success == true) {
                        val syncedResult = response.body()?.data
                        if (syncedResult != null) {
                            val updatedResult = QuizResultEntity.fromQuizResultResponseData(
                                syncedResult,
                                result.answerGiven,
                                isSynced = true
                            )
                            quizDao.insertQuizResult(updatedResult)
                            Log.d("QuizRepository", "Synced quiz result: ${result.quizId}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("QuizRepository", "Failed to sync result: ${result.quizId}", e)
                }
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error during sync", e)
        }
    }

    suspend fun syncAllUnsyncedData(token: String) {
        syncUnsyncedData(token)
    }

    suspend fun hasUnsyncedData(): Boolean {
        val unsyncedResults = quizDao.getUnsyncedQuizResults()
        return unsyncedResults.isNotEmpty()
    }
}