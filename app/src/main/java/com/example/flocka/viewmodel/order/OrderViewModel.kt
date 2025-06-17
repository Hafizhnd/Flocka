package com.example.flocka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.CreateOrderRequest
import com.example.flocka.data.model.EventItem
import com.example.flocka.data.model.OrderItem
import com.example.flocka.data.model.SpaceItem
import com.example.flocka.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _activeOrders = MutableStateFlow<List<OrderItem>>(emptyList())
    val activeOrders: StateFlow<List<OrderItem>> = _activeOrders.asStateFlow()

    private val _archivedOrders = MutableStateFlow<List<OrderItem>>(emptyList())
    val archivedOrders: StateFlow<List<OrderItem>> = _archivedOrders.asStateFlow()

    private val _orderCreationResult = MutableStateFlow<Result<OrderItem>?>(null)
    val orderCreationResult: StateFlow<Result<OrderItem>?> = _orderCreationResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoadingOrders = MutableStateFlow(false)
    val isLoadingOrders: StateFlow<Boolean> = _isLoadingOrders.asStateFlow()

    private val _isCreatingOrder = MutableStateFlow(false)
    val isCreatingOrder: StateFlow<Boolean> = _isCreatingOrder.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(false)
    val refreshTrigger: StateFlow<Boolean> = _refreshTrigger.asStateFlow()

    private val uiDateTimeFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val backendDateTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val backendUtcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val backendLocalDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val displayDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    class Factory(private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(orderRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    init {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect { orders ->
                val (active, archived) = classifyOrdersByDate(orders)
                _activeOrders.value = active
                _archivedOrders.value = archived
                Log.d("OrderViewModel", "Orders updated - Active: ${active.size}, Archived: ${archived.size}")
            }
        }
    }

    fun fetchMyOrders(token: String) {
        viewModelScope.launch {
            _isLoadingOrders.value = true
            _errorMessage.value = null

            try {
                orderRepository.fetchMyOrders(token).fold(
                    onSuccess = { orders ->
                        _refreshTrigger.update { !_refreshTrigger.value }
                        Log.d("OrderViewModel", "Orders fetched successfully: ${orders.size}")
                        _errorMessage.value = null
                    },
                    onFailure = { exception ->
                        if (exception is IOException) {
                            Log.d("OrderViewModel", "Network error - showing cached orders")
                        } else {
                            _errorMessage.value = exception.message ?: "Failed to fetch orders"
                            Log.e("OrderViewModel", "Failed to fetch orders", exception)
                        }
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                Log.e("OrderViewModel", "Unexpected error in fetchMyOrders", e)
            }

            _isLoadingOrders.value = false
        }
    }

    fun createOrderForSpaceSimple(
        token: String,
        space: SpaceItem
    ) {
        viewModelScope.launch {
            _isCreatingOrder.value = true
            _orderCreationResult.value = null
            _errorMessage.value = null

            try {
                val calendar = Calendar.getInstance()
                val formattedBookedStart = backendDateTimeFormatter.format(calendar.time)
                calendar.add(Calendar.HOUR_OF_DAY, 1)
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

                orderRepository.createOrder(token, request).fold(
                    onSuccess = { order ->
                        _orderCreationResult.value = Result.success(order)
                        fetchMyOrders(token) // Refresh orders
                        Log.d("OrderViewModel", "Space order created successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to book space"
                        _orderCreationResult.value = Result.failure(exception)
                        Log.e("OrderViewModel", "Failed to create space order", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                _orderCreationResult.value = Result.failure(e)
                Log.e("OrderViewModel", "Unexpected error in createOrderForSpaceSimple", e)
            }

            _isCreatingOrder.value = false
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
            _isCreatingOrder.value = true
            _orderCreationResult.value = null
            _errorMessage.value = null

            try {
                val startDateTimeStr = "$bookedDate $startTime"
                val parsedStartDateTime: Date? = try {
                    uiDateTimeFormatter.parse(startDateTimeStr)
                } catch (e: ParseException) {
                    null
                }

                if (parsedStartDateTime == null) {
                    val err = "Invalid date or time format for space booking. Received: date='$bookedDate', time='$startTime'. Expected: 'dd/MM/yyyy' and 'HH:mm'."
                    _errorMessage.value = err
                    _orderCreationResult.value = Result.failure(Exception(err))
                    _isCreatingOrder.value = false
                    return@launch
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

                orderRepository.createOrder(token, request).fold(
                    onSuccess = { order ->
                        _orderCreationResult.value = Result.success(order)
                        fetchMyOrders(token)
                        Log.d("OrderViewModel", "Space order created successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to book space"
                        _orderCreationResult.value = Result.failure(exception)
                        Log.e("OrderViewModel", "Failed to create space order", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                _orderCreationResult.value = Result.failure(e)
                Log.e("OrderViewModel", "Unexpected error in createOrderForSpace", e)
            }

            _isCreatingOrder.value = false
        }
    }

    fun createOrderForEvent(token: String, event: EventItem) {
        viewModelScope.launch {
            _isCreatingOrder.value = true
            _orderCreationResult.value = null
            _errorMessage.value = null

            try {
                if (event.startTime == null || event.endTime == null) {
                    val err = "Event start or end time is missing."
                    _errorMessage.value = err
                    _orderCreationResult.value = Result.failure(Exception(err))
                    _isCreatingOrder.value = false
                    return@launch
                }

                val parsedStartDateTime = parseGeneralBackendDateTime(event.startTime)
                val parsedEndDateTime = parseGeneralBackendDateTime(event.endTime)

                if (parsedStartDateTime == null || parsedEndDateTime == null) {
                    val err = "Invalid event start or end time format. Start: '${event.startTime}', End: '${event.endTime}'"
                    _errorMessage.value = err
                    _orderCreationResult.value = Result.failure(Exception(err))
                    _isCreatingOrder.value = false
                    return@launch
                }

                val formattedBookedStart = backendDateTimeFormatter.format(parsedStartDateTime)
                val formattedBookedEnd = backendDateTimeFormatter.format(parsedEndDateTime)

                val request = CreateOrderRequest(
                    itemType = "event",
                    itemId = event.eventId,
                    quantity = 1,
                    amountPaid = event.cost ?: 0.0,
                    bookedStartDatetime = formattedBookedStart,
                    bookedEndDatetime = formattedBookedEnd
                )

                Log.d("OrderViewModel", "Creating event order: $request")

                orderRepository.createOrder(token, request).fold(
                    onSuccess = { order ->
                        _orderCreationResult.value = Result.success(order)
                        fetchMyOrders(token)
                        Log.d("OrderViewModel", "Event order created successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to book event"
                        _orderCreationResult.value = Result.failure(exception)
                        Log.e("OrderViewModel", "Failed to create event order", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                _orderCreationResult.value = Result.failure(e)
                Log.e("OrderViewModel", "Unexpected error in createOrderForEvent", e)
            }

            _isCreatingOrder.value = false
        }
    }

    fun clearOperationResult() {
        _orderCreationResult.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    private fun parseGeneralBackendDateTime(dateTimeStr: String?): Date? {
        if (dateTimeStr.isNullOrBlank()) return null
        try {
            return backendUtcDateTimeFormat.parse(dateTimeStr)
        } catch (e: ParseException) {
            /* try next */
        }
        try {
            return backendLocalDateTimeFormat.parse(dateTimeStr)
        } catch (e: ParseException) {
            /* failed all */
        }
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

    fun classifyOrdersByDate(orders: List<OrderItem>): Pair<List<OrderItem>, List<OrderItem>> {
        val now = System.currentTimeMillis()
        val active = mutableListOf<OrderItem>()
        val expired = mutableListOf<OrderItem>()

        for (order in orders) {
            val endDate = parseGeneralBackendDateTime(order.bookedEndDatetime ?: order.bookedStartDatetime)
            if (endDate != null && endDate.time > now) {
                active.add(order)
            } else {
                expired.add(order)
            }
        }

        active.sortBy { parseGeneralBackendDateTime(it.bookedStartDatetime)?.time ?: Long.MAX_VALUE }
        expired.sortByDescending { parseGeneralBackendDateTime(it.bookedStartDatetime)?.time ?: Long.MIN_VALUE }

        return Pair(active, expired)
    }
}