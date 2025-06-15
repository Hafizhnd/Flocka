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
            val response = quizApi.getQuizQuestion("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val question = response.body()?.data
                if (question != null) {
                    quizDao.insertQuizQuestion(QuizEntity.fromQuizQuestion(question))
                    Result.success(question)
                } else {
                    Result.failure(Exception("No question data received"))
                }
            } else {
                val cachedQuestion = quizDao.getUnusedQuizQuestion()
                if (cachedQuestion != null) {
                    Log.d("QuizRepository", "Using cached question")
                    Result.success(cachedQuestion.toQuizQuestion())
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Failed to fetch question"))
                }
            }
        } catch (e: IOException) {
            val cachedQuestion = quizDao.getUnusedQuizQuestion()
            if (cachedQuestion != null) {
                Log.d("QuizRepository", "Network error - using cached question")
                Result.success(cachedQuestion.toQuizQuestion())
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error fetching quiz question", e)
            Result.failure(e)
        }
    }

    suspend fun submitQuizAnswer(
        token: String,
        quizId: String,
        answerGiven: Int
    ): Result<QuizResultResponseData> {
        return try {
            val request = QuizAnswerRequest(quizId, answerGiven)
            val response = quizApi.submitQuizAnswer("Bearer $token", request)

            if (response.isSuccessful && response.body()?.success == true) {
                val result = response.body()?.data
                if (result != null) {
                    quizDao.markQuizAsUsed(quizId)

                    quizDao.insertQuizResult(
                        QuizResultEntity(
                            quizId = result.quizId,
                            isCorrect = result.isCorrect,
                            correctAnswerText = result.correctAnswerText,
                            userStreak = result.userStreak,
                            answerGiven = answerGiven
                        )
                    )

                    Result.success(result)
                } else {
                    Result.failure(Exception("No result data received"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to submit answer"))
            }
        } catch (e: IOException) {
            Log.d("QuizRepository", "Offline - storing answer for later sync")
            quizDao.markQuizAsUsed(quizId)

            val offlineResult = QuizResultResponseData(
                quizId = quizId,
                isCorrect = false,
                correctAnswerText = null,
                userStreak = 0
            )

            Result.failure(Exception("Offline - answer will be synced later"))
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error submitting quiz answer", e)
            Result.failure(e)
        }
    }
}