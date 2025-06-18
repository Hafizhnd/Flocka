package com.example.flocka.viewmodel.event

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.EventItem
import com.example.flocka.data.repository.EventRepository
import com.example.flocka.data.repository.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

sealed class CreateEventState {
    object Idle : CreateEventState()
    object Loading : CreateEventState()
    data class Success(val event: EventItem?) : CreateEventState()
    data class Error(val message: String) : CreateEventState()
}

sealed class DeleteEventState {
    object Idle : DeleteEventState()
    object Loading : DeleteEventState()
    object Success : DeleteEventState()
    data class Error(val message: String) : DeleteEventState()
}

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    class Factory(private val repository: EventRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _events = MutableStateFlow<List<EventItem>>(emptyList())
    val events: StateFlow<List<EventItem>> = _events.asStateFlow()

    private val _selectedEvent = MutableStateFlow<EventItem?>(null)
    val selectedEvent: StateFlow<EventItem?> = _selectedEvent.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _createEventState = MutableStateFlow<CreateEventState>(CreateEventState.Idle)
    val createEventState: StateFlow<CreateEventState> = _createEventState.asStateFlow()

    private val _deleteEventState = MutableStateFlow<DeleteEventState>(DeleteEventState.Idle)
    val deleteEventState: StateFlow<DeleteEventState> = _deleteEventState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _fromCache = MutableStateFlow(false)
    val fromCache: StateFlow<Boolean> = _fromCache.asStateFlow()

    private val _hasPendingOperations = MutableStateFlow(false)
    val hasPendingOperations: StateFlow<Boolean> = _hasPendingOperations.asStateFlow()

    private val backendUtcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val backendLocalDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val backendOnlyDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val displayDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    init {
        checkPendingOperations()
    }

    fun fetchEvents(token: String, type: String = "upcoming") {
        viewModelScope.launch {
            repository.getEvents(token, type).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        _errorMessage.value = null
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _events.value = resource.data ?: emptyList()
                        _fromCache.value = resource.fromCache
                        _errorMessage.value = null

                        Log.w("EventViewModel", resource.data.toString())

                        checkPendingOperations()
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = resource.message
                        // Keep existing data if available
                        resource.data?.let { _events.value = it }
                    }
                }
            }
        }
    }

    fun fetchEventById(token: String, eventId: String) {
        Log.d("EventFlow", "EventViewModel.fetchEventById called with eventId: $eventId")
        viewModelScope.launch {
            repository.getEventById(token, eventId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _errorMessage.value = null
                    }
                    is Resource.Success -> {
                        _selectedEvent.value = resource.data
                        _errorMessage.value = null
                    }
                    is Resource.Error -> {
                        _errorMessage.value = resource.message
                        // Keep existing data if available
                        resource.data?.let { _selectedEvent.value = it }
                    }
                }
            }
        }
    }

    fun createEventWithStringDatesAndImage(
        token: String,
        name: String,
        description: String?,
        eventDateString: String, // Format: "yyyy-MM-dd"
        startTimeString: String, // Format: "HH:mm"
        endTimeString: String,   // Format: "HH:mm"
        location: String,
        imageUri: Uri? = null,
        cost: Double? = null
    ) {
        viewModelScope.launch {
            _createEventState.value = CreateEventState.Loading
            _errorMessage.value = null

            try {
                Log.d("EventViewModel", "Creating event with image URI: $imageUri")
                Log.d("EventViewModel", "Event details - Name: $name, Location: $location")
                Log.d("EventViewModel", "Date: $eventDateString, Start: $startTimeString, End: $endTimeString")
                val eventDate = backendOnlyDateFormat.parse(eventDateString)
                    ?: throw IllegalArgumentException("Invalid event date format")

                val calendar = Calendar.getInstance()
                calendar.time = eventDate

                val startTimeParts = startTimeString.split(":")
                if (startTimeParts.size != 2) throw IllegalArgumentException("Invalid start time format")
                calendar.set(Calendar.HOUR_OF_DAY, startTimeParts[0].toInt())
                calendar.set(Calendar.MINUTE, startTimeParts[1].toInt())
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startDateTime = calendar.time

                val endTimeParts = endTimeString.split(":")
                if (endTimeParts.size != 2) throw IllegalArgumentException("Invalid end time format")
                calendar.set(Calendar.HOUR_OF_DAY, endTimeParts[0].toInt())
                calendar.set(Calendar.MINUTE, endTimeParts[1].toInt())
                val endDateTime = calendar.time

                Log.d("EventViewModel", "Parsed dates - Event: $eventDate, Start: $startDateTime, End: $endDateTime")

                val result = repository.createEventWithImage(
                    token, name, description, eventDate, startDateTime, endDateTime, location, imageUri, cost
                )

                when (result) {
                    is Resource.Success -> {
                        Log.d("EventViewModel", "Event created successfully: ${result.data}")
                        _createEventState.value = CreateEventState.Success(result.data)
                        fetchEvents(token)
                    }
                    is Resource.Error -> {
                        Log.e("EventViewModel", "Error creating event: ${result.message}")
                        _createEventState.value = CreateEventState.Error(result.message ?: "Unknown error")
                    }
                    is Resource.Loading -> {
                    }
                }

            } catch (e: Exception) {
                Log.e("EventViewModel", "Exception in createEventWithStringDatesAndImage", e)
                _createEventState.value = CreateEventState.Error("Invalid date/time format: ${e.message}")
            }
        }
    }

    fun deleteEvent(token: String, eventId: String) {
        viewModelScope.launch {
            _deleteEventState.value = DeleteEventState.Loading
            _errorMessage.value = null

            val result = repository.deleteEvent(token, eventId)

            when (result) {
                is Resource.Success -> {
                    _deleteEventState.value = DeleteEventState.Success

                    _events.value = _events.value.filter { it.eventId != eventId }

                    if (_selectedEvent.value?.eventId == eventId) {
                        _selectedEvent.value = null
                    }

                    fetchEvents(token)
                }
                is Resource.Error -> {
                    _deleteEventState.value = DeleteEventState.Error(result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    fun syncPendingOperations(token: String) {
        viewModelScope.launch {
            repository.forceSync(token)
            checkPendingOperations()
            fetchEvents(token)
        }
    }

    private fun checkPendingOperations() {
        viewModelScope.launch {
            _hasPendingOperations.value = repository.hasPendingOperations()
        }
    }

    fun getOfflineEvents() {
        viewModelScope.launch {
            repository.getOfflineEvents().collect { events ->
                _events.value = events
                _fromCache.value = true
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

    fun resetCreateEventState() {
        _createEventState.value = CreateEventState.Idle
    }

    fun resetDeleteEventState() {
        _deleteEventState.value = DeleteEventState.Idle
    }
}

class EventViewModelFactory(
    private val repository: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}