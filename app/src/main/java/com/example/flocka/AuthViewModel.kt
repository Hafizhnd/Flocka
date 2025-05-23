package com.example.flocka

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var loginResult by mutableStateOf<Result<AuthResponse>?>(null)
    var registerResult by mutableStateOf<Result<AuthResponse>?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    loginResult = Result.success(response.body()!!)
                } else {
                    loginResult = Result.failure(Exception(response.body()?.message ?: "Login failed"))
                }
            } catch (e: Exception) {
                loginResult = Result.failure(e)
            }
        }
    }

    fun register(name: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.register(RegisterRequest(name, username, email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    registerResult = Result.success(response.body()!!)
                } else {
                    // Try to parse the error response manually
                    val errorMsg = try {
                        val errorBody = response.errorBody()?.string()
                        // naive JSON parsing (or use Gson if needed)
                        Regex("\"(message|error)\"\\s*:\\s*\"([^\"]+)\"").find(errorBody ?: "")
                            ?.groups?.get(2)?.value ?: "Registration failed"
                    } catch (e: Exception) {
                        "Registration failed"
                    }
                    registerResult = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                registerResult = Result.failure(e)
            }
        }
    }
}
