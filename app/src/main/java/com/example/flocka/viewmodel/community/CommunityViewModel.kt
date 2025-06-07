package com.example.flocka.viewmodel.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.model.CommunityItem
import com.example.flocka.data.model.CreateCommunityRequest
import com.example.flocka.data.model.UpdateCommunityRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

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

    fun clearActionResult() {
        _communityActionResult.value = null
    }

    fun fetchCommunities(token: String, type: String = "all") {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val response = if (type.equals("my", ignoreCase = true)) {
                    RetrofitClient.communityApi.getMyCommunities("Bearer $token")
                } else {
                    RetrofitClient.communityApi.getCommunities("Bearer $token")
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    if (type.equals("my", ignoreCase = true)) {
                        _myCommunities.value = response.body()?.data ?: emptyList()
                    } else {
                        _communities.value = response.body()?.data ?: emptyList()
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch communities"
                    _errorMessage.value = errorMsg
                    Log.e("CommunityVM", "Fetch ($type) Error: ${response.code()} - $errorMsg. Body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching communities: ${e.message}"
                Log.e("CommunityVM", "Fetch ($type) Exception", e)
            }
        }
    }

    fun fetchCommunityById(token: String, communityId: String) {
        viewModelScope.launch {
            _selectedCommunity.value = null
            _errorMessage.value = null
            try {
                val response = RetrofitClient.communityApi.getCommunityById("Bearer $token", communityId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _selectedCommunity.value = response.body()?.data
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch community details"
                    _errorMessage.value = errorMsg
                    Log.e("CommunityVM", "FetchById Error: ${response.code()} - $errorMsg. Body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching community: ${e.message}"
                Log.e("CommunityVM", "FetchById Exception", e)
            }
        }
    }

    fun createCommunity(token: String, name: String, description: String?, image: String? = null) {
        viewModelScope.launch {
            _communityActionResult.value = null
            _errorMessage.value = null
            try {
                val request = CreateCommunityRequest(name, description, image)
                val response = RetrofitClient.communityApi.createCommunity("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val newCommunity = response.body()?.data
                    if (newCommunity != null) {
                        _communityActionResult.value = Result.success(newCommunity)
                        fetchCommunities(token)
                        fetchCommunities(token, "my")
                    } else {
                        val errorMsg = "Community created but no data returned."
                        _errorMessage.value = errorMsg
                        _communityActionResult.value = Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to create community"
                    _errorMessage.value = errorMsg
                    _communityActionResult.value = Result.failure(Exception(errorMsg))
                    Log.e("CommunityVM", "CreateComm Error: ${response.code()} - $errorMsg. Body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error creating community: ${e.message}"
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
                val request = UpdateCommunityRequest(name, description, image)
                val response = RetrofitClient.communityApi.updateCommunity("Bearer $token", communityId, request)
                if (response.isSuccessful && response.body()?.success == true) {
                    val updatedCommunity = response.body()?.data
                    if (updatedCommunity != null) {
                        _communityActionResult.value = Result.success(updatedCommunity)
                    } else {

                        _communityActionResult.value = Result.success(CommunityItem(communityId, name ?: "", description, "", "", image, null, null, 0 )) // Create a dummy success if needed
                    }
                    fetchCommunityById(token, communityId)
                    fetchCommunities(token)
                    fetchCommunities(token, "my")
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to update community"
                    _errorMessage.value = errorMsg
                    _communityActionResult.value = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error updating community: ${e.message}"
                _communityActionResult.value = Result.failure(e)
            }
        }
    }
}