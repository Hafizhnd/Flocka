package com.example.flocka.viewmodel.space

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.SpaceItem
import com.example.flocka.data.repository.SpaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class SpaceViewModel(
    private val spaceRepository: SpaceRepository
) : ViewModel() {

    private val _spaces = MutableStateFlow<List<SpaceItem>>(emptyList())
    val spaces: StateFlow<List<SpaceItem>> = _spaces.asStateFlow()

    private val _selectedSpace = MutableStateFlow<SpaceItem?>(null)
    val selectedSpace: StateFlow<SpaceItem?> = _selectedSpace.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val backendTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
    val displayTimeFormat = SimpleDateFormat("h a", Locale.getDefault()) // "9 AM"

    class Factory(private val spaceRepository: SpaceRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SpaceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SpaceViewModel(spaceRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun fetchSpaces(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                Log.d("SpaceViewModel", "Fetching spaces with token...")
                spaceRepository.getSpaces(token).fold(
                    onSuccess = { fetchedSpaces ->
                        _spaces.value = fetchedSpaces
                        Log.d("SpaceViewModel", "Spaces fetched successfully: ${fetchedSpaces.size} items")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to fetch spaces"
                        Log.e("SpaceViewModel", "Failed to fetch spaces", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error fetching spaces: ${e.message}"
                Log.e("SpaceViewModel", "Unexpected error in fetchSpaces", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchSpaceById(token: String, spaceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedSpace.value = null
            _errorMessage.value = null
            try {
                Log.d("SpaceViewModel", "Fetching space by ID: $spaceId")
                spaceRepository.getSpaceById(token, spaceId).fold(
                    onSuccess = { space ->
                        _selectedSpace.value = space
                        Log.d("SpaceViewModel", "Space fetched successfully: ${space.name}")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to fetch space details"
                        Log.e("SpaceViewModel", "Failed to fetch space details", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error fetching space details: ${e.message}"
                Log.e("SpaceViewModel", "Unexpected error in fetchSpaceById", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCachedSpaces() {
        viewModelScope.launch {
            try {
                spaceRepository.getCachedSpaces().fold(
                    onSuccess = { cachedSpaces ->
                        _spaces.value = cachedSpaces
                        Log.d("SpaceViewModel", "Cached spaces loaded: ${cachedSpaces.size} items")
                    },
                    onFailure = { exception ->
                        Log.e("SpaceViewModel", "Failed to get cached spaces", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e("SpaceViewModel", "Unexpected error getting cached spaces", e)
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