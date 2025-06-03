package com.example.flocka.data.remote

import com.example.flocka.AuthResponse
import com.example.flocka.LoginRequest
import com.example.flocka.RegisterRequest
import com.example.flocka.UpdateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @PUT("api/auth/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateUserRequest
    ): Response<AuthResponse>

    @GET("api/auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<AuthResponse>

    @Multipart
    @PUT("api/auth/me/picture")
    suspend fun uploadProfilePicture(
        @Header("Authorization") token: String,
        @Part profile_picture: MultipartBody.Part
    ): Response<AuthResponse>
}
