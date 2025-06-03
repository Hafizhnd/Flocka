package com.example.flocka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.remote.RetrofitClient // Ensure correct import for RetrofitClient
import com.example.flocka.data.model.SpaceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SpaceViewModel : ViewModel() {
    private val _spaces = MutableStateFlow<List<SpaceItem>>(emptyList())
    val spaces: StateFlow<List<SpaceItem>> = _spaces.asStateFlow()

    private val _selectedSpace = MutableStateFlow<SpaceItem?>(null)
    val selectedSpace: StateFlow<SpaceItem?> = _selectedSpace.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    private val backendTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    val displayTimeFormat = SimpleDateFormat("h a", Locale.getDefault()) // "9 AM"



    fun fetchSpaces(token: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val response = RetrofitClient.spaceApi.getSpaces("Bearer $token")
                if (response.isSuccessful && response.body()?.success == true) {
                    _spaces.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Failed to fetch spaces"
                    Log.e("SpaceViewModel", "Fetch Spaces Error: ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching spaces: ${e.message}"
                Log.e("SpaceViewModel", "Fetch Spaces Exception", e)
            }
        }
    }

    fun fetchSpaceById(token: String, spaceId: String) {
        viewModelScope.launch {
            _selectedSpace.value = null
            _errorMessage.value = null
            try {
                val response = RetrofitClient.spaceApi.getSpaceById("Bearer $token", spaceId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _selectedSpace.value = response.body()?.data
                } else {
                    _errorMessage.value = response.body()?.message ?: "Failed to fetch space details"
                    Log.e("SpaceViewModel", "Fetch SpaceById Error: ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching space details: ${e.message}"
                Log.e("SpaceViewModel", "Fetch SpaceById Exception", e)
            }
        }
    }

    fun formatDisplayTime(timeString: String?): String {
        if (timeString.isNullOrBlank()) return "N/A"
        return try {
            backendTimeFormat.parse(timeString)?.let { displayTimeFormat.format(it) } ?: "N/A"
        } catch (e: ParseException) {
            Log.e("SpaceViewModel", "Time formatting error for $timeString", e)
            timeString
        }
    }
}