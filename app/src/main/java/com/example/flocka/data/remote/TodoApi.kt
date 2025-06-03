package com.example.flocka.data.api

import com.example.flocka.data.model.CreateTodoRequest
import com.example.flocka.data.model.TodoItem
import com.example.flocka.data.model.TodoSuccessResponse
import com.example.flocka.data.model.UpdateTodoRequest
import retrofit2.Response
import retrofit2.http.*

interface TodoApi {
    @POST("api/todos")
    suspend fun createTodo(
        @Header("Authorization") token: String,
        @Body request: CreateTodoRequest
    ): Response<TodoSuccessResponse<TodoItem>>

    @GET("api/todos")
    suspend fun getTodos(
        @Header("Authorization") token: String
    ): Response<TodoSuccessResponse<List<TodoItem>>>

    @PATCH("api/todos/{todoId}/toggle")
    suspend fun toggleTodoStatus(
        @Header("Authorization") token: String,
        @Path("todoId") todoId: String
    ): Response<TodoSuccessResponse<TodoItem>>

    @PUT("api/todos/{todoId}")
    suspend fun updateTodo(
        @Header("Authorization") token: String,
        @Path("todoId") todoId: String,
        @Body request: UpdateTodoRequest
    ): Response<TodoSuccessResponse<TodoItem>>

    @DELETE("api/todos/{todoId}")
    suspend fun deleteTodo(
        @Header("Authorization") token: String,
        @Path("todoId") todoId: String
    ): Response<TodoSuccessResponse<Unit>>
}