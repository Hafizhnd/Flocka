package com.example.flocka.viewmodel.event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.model.EventItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EventViewModel : ViewModel() {
    private val _events = MutableStateFlow<List<EventItem>>(emptyList())
    val events: StateFlow<List<EventItem>> = _events.asStateFlow()

    private val _selectedEvent = MutableStateFlow<EventItem?>(null)
    val selectedEvent: StateFlow<EventItem?> = _selectedEvent.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val backendUtcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val backendLocalDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    private val backendOnlyDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)



    val displayDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


    fun fetchEvents(token: String, type: String = "upcoming") {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val response = when (type.lowercase()) {
                    "upcoming" -> RetrofitClient.eventApi.getUpcomingEvents("Bearer $token")

                    else -> RetrofitClient.eventApi.getEvents("Bearer $token")
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    _events.value = response.body()?.data ?: emptyList()
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch events"
                    _errorMessage.value = errorMsg
                    Log.e("EventViewModel", "Fetch Events Error (${type}): ${response.code()} - $errorMsg. Body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching events: ${e.message}"
                Log.e("EventViewModel", "Fetch Events Exception (${type})", e)
            }
        }
    }

    fun fetchEventById(token: String, eventId: String) {
        Log.d("EventFlow", "EventViewModel.fetchEventById called with eventId: $eventId")
        viewModelScope.launch {
            _selectedEvent.value = null // Clear previous selection
            _errorMessage.value = null
            try {
                val response = RetrofitClient.eventApi.getEventById("Bearer $token", eventId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _selectedEvent.value = response.body()?.data
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch event details"
                    _errorMessage.value = errorMsg
                    Log.e("EventViewModel", "Fetch EventById Error: ${response.code()} - $errorMsg. Body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching event details: ${e.message}"
                Log.e("EventViewModel", "Fetch EventById Exception", e)
            }
        }
    }

    internal fun parseDateString(dateStr: String?): Date? {
        if (dateStr.isNullOrBlank()) return null

        try { return backendUtcDateTimeFormat.parse(dateStr) } catch (e: ParseException) { /* try next */ }

        try { return backendLocalDateTimeFormat.parse(dateStr) } catch (e: ParseException) { /* try next */ }

        try { return backendOnlyDateFormat.parse(dateStr) } catch (e: ParseException) { /* failed all */ }
        Log.w("EventViewModel", "Failed to parse date string: $dateStr")
        return null
    }

    fun formatDate(date: Date?, outputFormat: SimpleDateFormat): String {
        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun formatEventDisplayDate(eventDateString: String?): String {
        val parsedDate = parseDateString(eventDateString)
        return formatDate(parsedDate, displayDateFormat)
    }


    fun formatEventDisplayTime(dateTimeString: String?): String {
        val parsedDate = parseDateString(dateTimeString)
        return formatDate(parsedDate, displayTimeFormat)
    }

    fun getDisplayDateFromDateTimeString(dateTimeString: String?): String {
        val parsedDate = parseDateString(dateTimeString)
        return formatDate(parsedDate, displayDateFormat)
    }
}