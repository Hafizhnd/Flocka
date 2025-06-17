package com.example.flocka.viewmodel.todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flocka.data.model.TodoItem
import com.example.flocka.data.repository.TodoRepository
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
import java.util.TreeMap

fun Calendar.setToMidnight(): Calendar {
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
    return this
}

class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())

    private val _groupedTodos = MutableStateFlow<Map<String, List<TodoItem>>>(emptyMap())
    val groupedTodos: StateFlow<Map<String, List<TodoItem>>> = _groupedTodos.asStateFlow()

    private val _operationResult = MutableStateFlow<Result<String>?>(null)
    val operationResult: StateFlow<Result<String>?> = _operationResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val uiDisplayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val backendDateOnlyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val backendUtcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val backendLocalDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val groupHeaderDisplayFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
    val taskCardDisplayDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    val taskCardDisplayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    class Factory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TodoViewModel(todoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    internal fun parseBackendDateString(dateStr: String?): Date? {
        if (dateStr.isNullOrBlank()) return null
        try { return backendUtcDateTimeFormat.parse(dateStr) } catch (e: ParseException) { /* try next */ }
        try { return backendLocalDateTimeFormat.parse(dateStr) } catch (e: ParseException) { /* try next */ }
        try { return backendDateOnlyFormat.parse(dateStr) } catch (e: ParseException) { /* failed all */ }
        Log.w("TodoViewModel", "Failed to parse date string from backend: $dateStr")
        return null
    }

    fun formatDate(date: Date?, outputFormat: SimpleDateFormat): String {
        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun getDisplayDateFromDateTimeString(dateTimeString: String?): String {
        val parsedDate = parseBackendDateString(dateTimeString)
        return formatDate(parsedDate, taskCardDisplayDateFormat)
    }

    fun formatDisplayTimeFromDateTimeString(dateTimeString: String?): String {
        val parsedDate = parseBackendDateString(dateTimeString)
        return formatDate(parsedDate, taskCardDisplayTimeFormat)
    }

    fun formatGeneralDisplayDate(dateString: String?, desiredOutputFormat: SimpleDateFormat): String {
        val parsedDate = parseBackendDateString(dateString)
        return formatDate(parsedDate, desiredOutputFormat)
    }

    fun formatDisplayTimeFromTimeString(timeString: String?): String {
        if (timeString.isNullOrBlank()) return ""
        return try {
            val backendTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            backendTimeFormat.parse(timeString)?.let { taskCardDisplayTimeFormat.format(it) } ?: ""
        } catch (e: ParseException) {
            timeString.takeIf { it.length >= 5 }?.substring(0,5) ?: ""
        }
    }

    private fun groupAndSortTodos(todos: List<TodoItem>) {
        val todayCal = Calendar.getInstance().apply { setToMidnight() }
        val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1); setToMidnight() }

        val grouped = todos.groupBy { todo ->
            if (todo.date.isNullOrBlank()) {
                "No Date"
            } else {
                val parsedDate = parseBackendDateString(todo.date)
                if (parsedDate == null) "Invalid Date" else {
                    val todoDateCal = Calendar.getInstance().apply {
                        time = parsedDate
                        setToMidnight()
                    }
                    when {
                        todoDateCal.before(todayCal) && !todo.isDone -> "Past Due"
                        todoDateCal == todayCal -> "Today"
                        todoDateCal == tomorrowCal -> "Tomorrow"
                        todoDateCal.after(tomorrowCal) -> groupHeaderDisplayFormat.format(todoDateCal.time)
                        else -> {
                            if (todoDateCal.before(todayCal)) "Completed Earlier"
                            else groupHeaderDisplayFormat.format(todoDateCal.time)
                        }
                    }
                }
            }
        }.mapValues { entry ->
            entry.value.sortedWith(compareBy(nullsLast()) { it.startTime })
        }

        val groupOrder = listOf("Past Due", "Today", "Tomorrow")
        val sortedGroupedTodos = TreeMap<String, List<TodoItem>>(Comparator { key1, key2 ->
            val index1 = groupOrder.indexOf(key1)
            val index2 = groupOrder.indexOf(key2)
            when {
                key1 == key2 -> 0
                index1 != -1 && index2 != -1 -> index1.compareTo(index2)
                index1 != -1 -> -1
                index2 != -1 -> 1
                key1 == "No Date" -> 1
                key2 == "No Date" -> -1
                key1 == "Invalid Date" || key1 == "Scheduled" || key1 == "Completed Earlier" -> {
                    if (key2 == "No Date" || key2 == "Invalid Date" || key2 == "Scheduled" || key2 == "Completed Earlier") key1.compareTo(key2) else 1
                }
                key2 == "Invalid Date" || key2 == "Scheduled" || key2 == "Completed Earlier" -> -1
                else -> {
                    try {
                        val date1 = groupHeaderDisplayFormat.parse(key1)
                        val date2 = groupHeaderDisplayFormat.parse(key2)
                        date1?.compareTo(date2) ?: key1.compareTo(key2)
                    } catch (e: Exception) { key1.compareTo(key2) }
                }
            }
        })
        sortedGroupedTodos.putAll(grouped)
        _groupedTodos.value = sortedGroupedTodos
    }

    fun fetchTodos(token: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                Log.d("TodoViewModel", "Fetching todos with token...")
                todoRepository.getTodos(token).fold(
                    onSuccess = { fetchedTodos ->
                        _todos.value = fetchedTodos
                        groupAndSortTodos(fetchedTodos)
                        Log.d("TodoViewModel", "Todos fetched successfully: ${fetchedTodos.size} items")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to fetch todos"
                        _groupedTodos.value = emptyMap()
                        Log.e("TodoViewModel", "Failed to fetch todos", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error fetching todos: ${e.message}"
                _groupedTodos.value = emptyMap()
                Log.e("TodoViewModel", "Unexpected error in fetchTodos", e)
            }
        }
    }

    fun addTodo(
        token: String, taskTitle: String, taskDescription: String?,
        startTime: String, endTime: String, date: String
    ) {
        viewModelScope.launch {
            _operationResult.value = null
            _errorMessage.value = null
            try {
                val formattedStartTime = if (startTime.isNotBlank()) "$startTime:00" else null
                val formattedEndTime = if (endTime.isNotBlank()) "$endTime:00" else null
                val formattedDate = if (date.isNotBlank()) {
                    try { uiDisplayDateFormat.parse(date)?.let { backendDateOnlyFormat.format(it) } }
                    catch (e: Exception) { null }
                } else { null }

                if (date.isNotBlank() && formattedDate == null) {
                    val err = "Invalid date format. Please use dd/MM/yyyy."
                    _errorMessage.value = err
                    _operationResult.value = Result.failure(IllegalArgumentException(err))
                    return@launch
                }

                todoRepository.createTodo(
                    token, taskTitle, taskDescription,
                    formattedStartTime, formattedEndTime, formattedDate
                ).fold(
                    onSuccess = { message ->
                        _operationResult.value = Result.success(message)
                        fetchTodos(token)
                        Log.d("TodoViewModel", "Todo added successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to add task"
                        _operationResult.value = Result.failure(exception)
                        Log.e("TodoViewModel", "Failed to add todo", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error adding task: ${e.message}"
                _operationResult.value = Result.failure(e)
                Log.e("TodoViewModel", "Unexpected error in addTodo", e)
            }
        }
    }

    fun updateTodo(
        token: String, todoId: String, taskTitle: String?, taskDescription: String?,
        startTime: String?, endTime: String?, date: String?
    ) {
        viewModelScope.launch {
            _operationResult.value = null
            _errorMessage.value = null
            try {
                val formattedStartTime = startTime?.takeIf { it.isNotBlank() }?.let { "$it:00" }
                val formattedEndTime = endTime?.takeIf { it.isNotBlank() }?.let { "$it:00" }
                val formattedDate = date?.takeIf { it.isNotBlank() }?.let {
                    try { uiDisplayDateFormat.parse(it)?.let { d -> backendDateOnlyFormat.format(d) } }
                    catch (e: Exception) { null }
                }

                if (date != null && date.isNotBlank() && formattedDate == null) {
                    val err = "Invalid date format for update. Please use dd/MM/yyyy."
                    _errorMessage.value = err
                    _operationResult.value = Result.failure(IllegalArgumentException(err))
                    return@launch
                }

                todoRepository.updateTodo(
                    token, todoId, taskTitle, taskDescription,
                    formattedStartTime, formattedEndTime, formattedDate
                ).fold(
                    onSuccess = { message ->
                        _operationResult.value = Result.success(message)
                        fetchTodos(token)
                        Log.d("TodoViewModel", "Todo updated successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to update task"
                        _operationResult.value = Result.failure(exception)
                        Log.e("TodoViewModel", "Failed to update todo", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error updating task: ${e.message}"
                _operationResult.value = Result.failure(e)
                Log.e("TodoViewModel", "Unexpected error in updateTodo", e)
            }
        }
    }

    fun deleteTodoOnCheck(token: String, todoId: String) {
        viewModelScope.launch {
            _operationResult.value = null
            _errorMessage.value = null
            try {
                todoRepository.deleteTodo(token, todoId).fold(
                    onSuccess = { message ->
                        _operationResult.value = Result.success(message)
                        fetchTodos(token)
                        Log.d("TodoViewModel", "Todo deleted successfully")
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message ?: "Failed to delete task"
                        _operationResult.value = Result.failure(exception)
                        Log.e("TodoViewModel", "Failed to delete todo", exception)
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error deleting task: ${e.message}"
                _operationResult.value = Result.failure(e)
                Log.e("TodoViewModel", "Unexpected error in deleteTodoOnCheck", e)
            }
        }
    }

    fun clearOperationResult() {
        _operationResult.value = null
        _errorMessage.value = null
    }
}