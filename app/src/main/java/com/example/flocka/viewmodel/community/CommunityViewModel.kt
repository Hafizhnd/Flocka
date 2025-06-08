package com.example.flocka.viewmodel.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.CommunityItem
import com.example.flocka.data.repository.CommunityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val repository: CommunityRepository
) : ViewModel() {

    private val _communities = MutableStateFlow<List<CommunityItem>>(emptyList())
    val communities: StateFlow<List<CommunityItem>> = _communities.asStateFlow()

    private val _myCommunities = MutableStateFlow<List<CommunityItem>>(emptyList())
    val myCommunities: StateFlow<List<CommunityItem>> = _myCommunities.asStateFlow()

    private val _selectedCommunity = MutableStateFlow<CommunityItem?>(null)
    val selectedCommunity: StateFlow<CommunityItem?> = _selectedCommunity.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _communityActionResult = MutableStateFlow<Result<CommunityItem>?>(null)
    val communityActionResult: StateFlow<Result<CommunityItem>?> = _communityActionResult.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllCommunities()
                .catch { e ->
                    _errorMessage.value = "Error loading communities: ${e.message}"
                }
                .collect { communities ->
                    _communities.value = communities
                }
        }
    }

    fun clearActionResult() {
        _communityActionResult.value = null
    }

    fun fetchCommunities(token: String, type: String = "all") {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                repository.refreshCommunities(token)
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching communities: ${e.message}"
                Log.e("CommunityVM", "Fetch ($type) Exception", e)
            }
        }
    }

    fun fetchCommunityById(token: String, communityId: String) {
        viewModelScope.launch {
            _selectedCommunity.value = null
            _errorMessage.value = null
            try {
                val community = repository.getCommunityById(token, communityId)
                _selectedCommunity.value = community
                if (community == null) {
                    _errorMessage.value = "Community not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching community: ${e.message}"
                Log.e("CommunityVM", "FetchById Exception", e)
            }
        }
    }

    fun createCommunity(token: String, name: String, description: String?, image: String? = null) {
        viewModelScope.launch {
            _communityActionResult.value = null
            _errorMessage.value = null
            try {
                val result = repository.createCommunity(token, name, description, image)
                _communityActionResult.value = result
                if (result.isFailure) {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error creating community: ${e.message}"
                _communityActionResult.value = Result.failure(e)
                Log.e("CommunityVM", "CreateComm Exception", e)
            }
        }
    }

    fun updateCommunity(token: String, communityId: String, name: String?, description: String?, image: String? = null) {
        viewModelScope.launch {
            _communityActionResult.value = null
            _errorMessage.value = null
            try {
                val result = repository.updateCommunity(token, communityId, name, description, image)
                _communityActionResult.value = result
                if (result.isFailure) {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error updating community: ${e.message}"
                _communityActionResult.value = Result.failure(e)
                Log.e("CommunityVM", "UpdateComm Exception", e)
            }
        }
    }

    fun syncUnsyncedData(token: String) {
        viewModelScope.launch {
            try {
                repository.syncUnsyncedCommunities(token)
            } catch (e: Exception) {
                Log.e("CommunityVM", "Sync Exception", e)
            }
        }
    }

    class Factory(private val repository: CommunityRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
                return CommunityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}