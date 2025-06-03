package com.example.flocka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.CreateOrderRequest
import com.example.flocka.data.model.EventItem
import com.example.flocka.data.model.OrderItem
import com.example.flocka.data.model.SpaceItem
import com.example.flocka.data.remote.RetrofitClient
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

class OrderViewModel : ViewModel() {
    private val _activeOrders = MutableStateFlow<List<OrderItem>>(emptyList())
    val activeOrders: StateFlow<List<OrderItem>> = _activeOrders.asStateFlow()

    private val _archivedOrders = MutableStateFlow<List<OrderItem>>(emptyList())
    val archivedOrders: StateFlow<List<OrderItem>> = _archivedOrders.asStateFlow()

    private val _orderCreationResult = MutableStateFlow<Result<OrderItem>?>(null)
    val orderCreationResult: StateFlow<Result<OrderItem>?> = _orderCreationResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val uiDateTimeFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val backendDateTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    private val backendUtcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val backendLocalDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    val displayDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun createOrderForSpaceSimple(
        token: String,
        space: SpaceItem
        // Defaulting to a 1-hour booking starting now
    ) {
        viewModelScope.launch {
            _orderCreationResult.value = null; _errorMessage.value = null
            try {
                val calendar = Calendar.getInstance() // Current time
                val formattedBookedStart = backendDateTimeFormatter.format(calendar.time)
                calendar.add(Calendar.HOUR_OF_DAY, 1) // Default 1 hour duration
                val formattedBookedEnd = backendDateTimeFormatter.format(calendar.time)

                val quantity = 1 // 1 hour
                val amountPaid = space.costPerHour?.times(quantity) ?: 0.0

                val request = CreateOrderRequest(
                    itemType = "space",
                    itemId = space.spaceId,
                    quantity = quantity,
                    amountPaid = amountPaid,
                    bookedStartDatetime = formattedBookedStart,
                    bookedEndDatetime = formattedBookedEnd
                )
                Log.d("OrderViewModel", "Creating simplified space order: $request")
                val response = RetrofitClient.orderApi.createOrder("Bearer $token", request)
                if (response.isSuccessful && response.body()?.success == true && response.body()?.data != null) {
                    _orderCreationResult.value = Result.success(response.body()!!.data!!)
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to book space (Code: ${response.code()})"
                    _errorMessage.value = errorMsg
                    _orderCreationResult.value = Result.failure(Exception(errorMsg))
                    Log.e("OrderViewModel", "Create Simplified Space Order Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error creating space order: ${e.message}"
                _orderCreationResult.value = Result.failure(e)
                Log.e("OrderViewModel", "Create Simplified Space Order Exception", e)
            }
        }
    }

    fun createOrderForSpace(
        token: String,
        space: SpaceItem,
        bookedDate: String,
        startTime: String,
        durationHours: Int
    ) {
        viewModelScope.launch {
            _orderCreationResult.value = null; _errorMessage.value = null
            try {
                val startDateTimeStr = "$bookedDate $startTime"
                val parsedStartDateTime: Date? = try { uiDateTimeFormatter.parse(startDateTimeStr) } catch (e: ParseException) { null }

                if (parsedStartDateTime == null) {
                    val err = "Invalid date or time for space booking."
                    _errorMessage.value = err
                    _orderCreationResult.value = Result.failure(Exception(err)); return@launch
                }

                val calendar = Calendar.getInstance().apply { time = parsedStartDateTime }
                val formattedBookedStart = backendDateTimeFormatter.format(calendar.time)
                calendar.add(Calendar.HOUR_OF_DAY, durationHours)
                val formattedBookedEnd = backendDateTimeFormatter.format(calendar.time)

                val totalCost = (space.costPerHour ?: 0.0) * durationHours.coerceAtLeast(1)

                val request = CreateOrderRequest(
                    itemType = "space",
                    itemId = space.spaceId,
                    quantity = durationHours,
                    amountPaid = totalCost,
                    bookedStartDatetime = formattedBookedStart,
                    bookedEndDatetime = formattedBookedEnd
                )
                Log.d("OrderViewModel", "Creating space order: $request")
                val response = RetrofitClient.orderApi.createOrder("Bearer $token", request)
                if (response.isSuccessful && response.body()?.success == true && response.body()?.data != null) {
                    _orderCreationResult.value = Result.success(response.body()!!.data!!)
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to book space (Code: ${response.code()})"
                    _errorMessage.value = errorMsg
                    _orderCreationResult.value = Result.failure(Exception(errorMsg))
                    Log.e("OrderViewModel", "Create Space Order Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error creating space order: ${e.message}"
                _orderCreationResult.value = Result.failure(e)
                Log.e("OrderViewModel", "Create Space Order Exception", e)
            }
        }
    }

    fun fetchMyOrders(token: String, active: Boolean) {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val statusQuery = if (active) "active" else "archived"
                Log.d("OrderViewModel", "Fetching my orders with token: ${token.take(10)}..., status: $statusQuery")
                val response = RetrofitClient.orderApi.getMyOrders("Bearer $token", statusQuery)
                if (response.isSuccessful && response.body()?.success == true) {
                    val orders = response.body()?.data ?: emptyList()
                    if (active) {
                        _activeOrders.value = orders
                    } else {
                        _archivedOrders.value = orders
                    }
                    Log.d("OrderViewModel", "Fetched ${orders.size} ${if(active) "active" else "archived"} orders.")
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch orders (Code: ${response.code()})"
                    _errorMessage.value = errorMsg
                    Log.e("OrderViewModel", "Fetch MyOrders Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching orders: ${e.message}"
                Log.e("OrderViewModel", "Fetch MyOrders Exception", e)
            }
        }
    }

    fun clearOperationResult() {
        _orderCreationResult.value = null
    }

    private fun parseGeneralBackendDateTime(dateTimeStr: String?): Date? {
        if (dateTimeStr.isNullOrBlank()) return null
        try { return backendUtcDateTimeFormat.parse(dateTimeStr) } catch (e: ParseException) { /* try next */ }
        try { return backendLocalDateTimeFormat.parse(dateTimeStr) } catch (e: ParseException) { /* failed all */ }
        Log.w("OrderViewModel", "Failed to parse general datetime string: $dateTimeStr")
        return null
    }

    fun getDisplayDateForOrder(dateTimeString: String?): String {
        val parsedDate = parseGeneralBackendDateTime(dateTimeString)
        return parsedDate?.let { displayDateFormat.format(it) } ?: "N/A"
    }

    fun getDisplayTimeForOrder(dateTimeString: String?): String {
        val parsedDate = parseGeneralBackendDateTime(dateTimeString)
        return parsedDate?.let { displayTimeFormat.format(it) } ?: "N/A"
    }
}