package com.example.flocka

data class User(
    val uid: String,
    val name: String,
    val username: String,
    val email: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: User? = null
)
