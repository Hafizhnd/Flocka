package com.example.flocka

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid")
    val uid: String,

    @SerializedName("name")
    val name: String?, // Can be null initially before setup

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    // Add all the other fields that your backend sends
    @SerializedName("profession")
    val profession: String?,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("age")
    val age: Int?,

    @SerializedName("bio")
    val bio: String?
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
    @SerializedName("name") val name: String?,
    val profession: String?,
    val gender: String?,
    val age: Int?,
    val bio: String?
)
