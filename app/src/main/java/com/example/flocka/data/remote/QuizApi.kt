package com.example.flocka.data.remote

import com.example.flocka.data.model.QuizAnswerRequest
import com.example.flocka.data.model.QuizQuestion
import com.example.flocka.data.model.QuizResultResponse
import com.example.flocka.data.model.GenericApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface QuizApi {
    @GET("api/quizzes/question")
    suspend fun getQuizQuestion(
        @Header("Authorization") token: String
    ): Response<GenericApiResponse<QuizQuestion>>

    @POST("api/quizzes/submit")
    suspend fun submitQuizAnswer(
        @Header("Authorization") token: String,
        @Body answerRequest: QuizAnswerRequest
    ): Response<QuizResultResponse>
}