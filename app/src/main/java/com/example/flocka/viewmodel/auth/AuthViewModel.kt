package com.example.flocka

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

class AuthViewModel : ViewModel() {
    var loginResult by mutableStateOf<Result<AuthResponse>?>(null)
    var registerResult by mutableStateOf<Result<AuthResponse>?>(null)
    var updateProfileResult by mutableStateOf<Result<AuthResponse>?>(null)
    var uploadResult by mutableStateOf<Result<AuthResponse>?>(null)

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    private val _profileError = MutableStateFlow<String?>(null)
    val profileError: StateFlow<String?> = _profileError

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


    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                // 'name' is no longer passed in RegisterRequest
                val response = RetrofitClient.authApi.register(RegisterRequest(username, email, password))
                if (response.isSuccessful && response.body()?.data != null) { // Check for data wrapper
                    registerResult = Result.success(response.body()!!)
                } else {
                    val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Registration failed"
                    registerResult = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                registerResult = Result.failure(e)
            }
        }
    }

    fun updateProfile(
        token: String,
        name: String, // Combined name
        profession: String,
        gender: String,
        age: String,
        bio: String,
        interestIds: List<String>
    ) {
        viewModelScope.launch {
            try {
                val request = UpdateUserRequest(
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
        viewModelScope.launch {
            try {
                // The token passed from NavGraph needs "Bearer " prepended
                val response = RetrofitClient.authApi.getProfile("Bearer $token")
                if (response.isSuccessful && response.body()?.data?.user != null) {
                    _userProfile.value = response.body()!!.data!!.user
                } else {
                    _profileError.value = response.body()?.message ?: "Failed to fetch profile."
                }
            } catch (e: Exception) {
                _profileError.value = e.message ?: "An unknown error occurred."
            }
        }
    }

    fun uploadProfilePicture(context: Context, token: String, imageUri: Uri) {
        viewModelScope.launch {
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
}
