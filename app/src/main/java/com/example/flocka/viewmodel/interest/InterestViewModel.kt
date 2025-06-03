package com.example.flocka.viewmodel.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.model.Interest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InterestViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Interest>>(emptyList())
    val searchResults: StateFlow<List<Interest>> = _searchResults

    fun searchInterests(token: String, query: String) {
        if (query.length < 2) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                val response = RetrofitClient.interestApi.searchInterests("Bearer $token", query)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {

            }
        }
    }
}