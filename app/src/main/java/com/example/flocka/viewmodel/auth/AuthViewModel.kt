package com.example.flocka.viewmodel.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.BulkInterestRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Log
import com.example.flocka.AuthResponse
import com.example.flocka.LoginRequest
import com.example.flocka.RegisterRequest
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.UpdateUserRequest
import com.example.flocka.User
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    var loginResult by mutableStateOf<Result<AuthResponse>?>(null)
    var registerResult by mutableStateOf<Result<AuthResponse>?>(null)
    var updateProfileResult by mutableStateOf<Result<AuthResponse>?>(null)
    var uploadResult by mutableStateOf<Result<AuthResponse>?>(null)

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private val _profileError = MutableStateFlow<String?>(null)
    val profileError: StateFlow<String?> = _profileError.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    fun setToken(newToken: String?) {
        Log.d("AuthViewModel", "Setting token: ${newToken?.take(10)}...")
        _token.value = newToken
        if (!newToken.isNullOrBlank()) {
            fetchUserProfile(newToken)
        } else {
            _userProfile.value = null
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    val authData = response.body()?.data
                    if (authData?.user != null && authData.token != null) {
                        loginResult = Result.success(response.body()!!)
                        setToken(authData.token)
                        _userProfile.value = authData.user
                        Log.d("AuthViewModel", "Login successful. Token set. User: ${authData.user.username}")
                    } else {
                        val errorMsg = response.body()?.message ?: "Login succeeded but data is incomplete."
                        Log.e("AuthViewModel", "Login error (incomplete data): $errorMsg")
                        loginResult = Result.failure(Exception(errorMsg))
                        setToken(null)
                    }
                } else {
                    val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Login failed"
                    loginResult = Result.failure(Exception(errorMsg))
                    setToken(null)
                }
            } catch (e: Exception) {
                loginResult = Result.failure(e)
                setToken(null)
            }
        }
    }


    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.register(RegisterRequest(username, email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    val authData = response.body()?.data
                    if (authData?.user != null && authData.token != null) {
                        registerResult = Result.success(response.body()!!)
                        setToken(authData.token)
                        _userProfile.value = authData.user
                        Log.d("AuthViewModel", "Registration successful. Token set. User: ${authData.user.username}")
                    } else {
                        val errorMsg = response.body()?.message ?: "Registration succeeded but data is incomplete."
                        Log.e("AuthViewModel", "Register error (incomplete data): $errorMsg")
                        registerResult = Result.failure(Exception(errorMsg))
                        setToken(null)
                    }
                } else {
                    val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Registration failed"
                    registerResult = Result.failure(Exception(errorMsg))
                    setToken(null)
                }
            } catch (e: Exception) {
                registerResult = Result.failure(e)
                setToken(null)
            }
        }
    }

    fun updateProfile(
        token: String,
        username: String,
        name: String,
        profession: String,
        gender: String,
        age: String,
        bio: String,
        interestIds: List<String>
    ) {
        viewModelScope.launch {
            updateProfileResult = null
            _profileError.value = null
            try {
                val request = UpdateUserRequest(
                    username = username.takeIf { it.isNotBlank() },
                    name = name.takeIf { it.isNotBlank() }, // Use combined name
                    profession = profession.takeIf { it.isNotBlank() },
                    gender = gender.takeIf { it.isNotBlank() },
                    age = age.toIntOrNull(),
                    bio = bio.takeIf { it.isNotBlank() }
                )
                val response = RetrofitClient.authApi.updateProfile("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    if (interestIds.isNotEmpty()) {
                        val interestRequest = BulkInterestRequest(interestIds)
                        RetrofitClient.interestApi.bulkAddUserInterests("Bearer $token", interestRequest)
                    }
                    updateProfileResult = Result.success(response.body()!!)
                } else {
                    val errorMsg = response.body()?.message ?: "Profile update failed"
                    updateProfileResult = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                updateProfileResult = Result.failure(e)
            }
        }
    }

    fun fetchUserProfile(token: String) {
        if (token.isBlank()) {
            Log.d("AuthViewModel", "fetchUserProfile skipped: token is blank.")
            return
        }
        if (_userProfile.value != null && _token.value == token) {
            Log.d("AuthViewModel", "fetchUserProfile skipped: profile already loaded for current token.")
            return
        }
        Log.d("AuthViewModel", "Fetching user profile with token.")
        viewModelScope.launch {
            _profileError.value = null
            try {
                val response = RetrofitClient.authApi.getProfile("Bearer $token")
                if (response.isSuccessful && response.body()?.data?.user != null) {
                    _userProfile.value = response.body()!!.data!!.user
                    Log.d("AuthViewModel", "User profile fetched: ${_userProfile.value?.username}")
                } else {
                    _profileError.value = response.body()?.message ?: "Failed to fetch profile (Code: ${response.code()})."
                    Log.e("AuthViewModel", "Fetch UserProfile Error: ${response.code()} - ${response.body()?.message}. ErrorBody: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _profileError.value = "Network error fetching profile: ${e.message}"
                Log.e("AuthViewModel", "Fetch UserProfile Exception", e)
            }
        }
    }

    fun uploadProfilePicture(context: Context, token: String, imageUri: Uri) {
        viewModelScope.launch {
            uploadResult = null
            _profileError.value = null
            try {
                // Get an InputStream from the Uri
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    val fileBytes = inputStream.readBytes()

                    val requestFile = fileBytes.toRequestBody(
                        context.contentResolver.getType(imageUri)?.toMediaTypeOrNull()
                    )

                    val body = MultipartBody.Part.createFormData(
                        "profile_picture",
                        "profile_picture.jpg",
                        requestFile
                    )

                    val response = RetrofitClient.authApi.uploadProfilePicture("Bearer $token", body)

                    if (response.isSuccessful) {
                        uploadResult = Result.success(response.body()!!)
                    } else {
                        uploadResult = Result.failure(Exception("Upload failed"))
                    }
                }
            } catch (e: Exception) {
                uploadResult = Result.failure(e)
            }
        }
    }
    fun logout() {
        Log.d("AuthViewModel", "Logging out user and clearing token.")
        setToken(null)
        loginResult = null
        registerResult = null
        updateProfileResult = null
        uploadResult = null
    }
}
