package com.example.flocka

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid")
    val uid: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("profile_image_url")
    val profile_image_url: String?,

    @SerializedName("profession")
    val profession: String?,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("age")
    val age: Int?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("current_streak")
    val currentStreak: Int?,

    @SerializedName("last_quiz_completed_date")
    val lastQuizCompletedDate: String?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class AuthData(
    @SerializedName("user")
    val user: User?,

    @SerializedName("token")
    val token: String?
)

data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: AuthData? = null
)

data class UpdateUserRequest(
    @SerializedName("username") val username: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profession") val profession: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("bio") val bio: String?
)
