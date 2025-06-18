package com.example.flocka.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import com.example.flocka.data.local.dao.EventDao
import com.example.flocka.data.local.dao.PendingOperationDao
import com.example.flocka.data.local.entity.EventEntity
import com.example.flocka.data.local.entity.PendingOperationEntity
import com.example.flocka.data.local.entity.toEntity
import com.example.flocka.data.local.entity.toEventItem
import com.example.flocka.data.model.CreateEventRequest
import com.example.flocka.data.model.EventItem
import com.example.flocka.data.remote.EventApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class EventRepository(
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val pendingOperationDao: PendingOperationDao,
    private val context: Context
) {
    private val gson = Gson()

    companion object {
        private const val TAG = "EventRepository"
        private const val OPERATION_CREATE = "CREATE"
        private const val OPERATION_DELETE = "DELETE"
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun getEvents(token: String, type: String = "upcoming"): Flow<Resource<List<EventItem>>> = flow {
        emit(Resource.Loading())

        val cachedEvents = eventDao.getAllEvents().firstOrNull()
        cachedEvents?.let { entities ->
            val eventItems = entities.map { it.toEventItem() }
            emit(Resource.Success(eventItems, fromCache = true))
        }

        if (isNetworkAvailable()) {
            try {
                syncPendingOperations(token)

                val response = when (type.lowercase()) {
                    "upcoming" -> eventApi.getUpcomingEvents("Bearer $token")
                    else -> eventApi.getUpcomingEvents("Bearer $token")
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    val serverEvents = response.body()?.data ?: emptyList()

                    val allLocalEvents = eventDao.getAllEventsSync()
                    allLocalEvents.filter { !it.isSynced && !it.isDeleted }

                    val serverEventEntities = serverEvents.map {
                        it.toEntity().copy(isSynced = true)
                    }

                    val serverEventIds = serverEvents.map { it.eventId }.toSet()
                    val localSyncedEvents = allLocalEvents.filter { it.isSynced }

                    localSyncedEvents.forEach { localEvent ->
                        if (localEvent.eventId !in serverEventIds) {
                            eventDao.deleteEventPermanently(localEvent.eventId)
                        }
                    }

                    eventDao.insertEvents(serverEventEntities)

                    val allCurrentEvents = eventDao.getAllEventsSync()
                    val eventItems = allCurrentEvents
                        .filter { !it.isDeleted }
                        .map { it.toEventItem() }

                    emit(Resource.Success(eventItems, fromCache = false))
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch events"
                    emit(Resource.Error(errorMsg))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Network error: ${e.message}"))
            }
        }
    }.catch { e ->
        emit(Resource.Error("Error: ${e.message}"))
    }.flowOn(Dispatchers.IO)

    fun getEventById(token: String, eventId: String): Flow<Resource<EventItem>> = flow {
        emit(Resource.Loading())

        try {
            val cachedEvent = eventDao.getEventById(eventId)
            if (cachedEvent != null) {
                emit(Resource.Success(cachedEvent.toEventItem(), fromCache = true))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching cached event", e)
        }

        if (isNetworkAvailable()) {
            try {
                val response = eventApi.getEventById("Bearer $token", eventId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val event = response.body()?.data
                    if (event != null) {
                        // Cache the event and mark as synced
                        eventDao.insertEvent(event.toEntity().copy(isSynced = true))
                        emit(Resource.Success(event, fromCache = false))
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to fetch event"
                    emit(Resource.Error(errorMsg))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Network error: ${e.message}"))
            }
        }
    }.catch { e ->
        emit(Resource.Error("Error: ${e.message}"))
    }.flowOn(Dispatchers.IO)

    suspend fun createEventWithImage(
        token: String,
        name: String,
        description: String?,
        eventDate: Date,
        startTime: Date,
        endTime: Date,
        location: String,
        imageUri: Uri? = null,
        cost: Double? = null
    ): Resource<EventItem> = withContext(Dispatchers.IO) {
        try {
            if (name.isBlank()) {
                return@withContext Resource.Error("Event name is required")
            }
            if (location.isBlank()) {
                return@withContext Resource.Error("Location is required")
            }
            if (endTime.time <= startTime.time) {
                return@withContext Resource.Error("End time must be after start time")
            }
            if (cost != null && cost < 0) {
                return@withContext Resource.Error("Cost must be non-negative")
            }

            val backendUtcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val backendOnlyDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            val eventDateStr = backendOnlyDateFormat.format(eventDate)
            val startTimeStr = backendUtcDateTimeFormat.format(startTime)
            val endTimeStr = backendUtcDateTimeFormat.format(endTime)

            if (isNetworkAvailable()) {
                try {
                    val nameRequestBody = name.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                    val descriptionRequestBody = description?.trim()?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val eventDateRequestBody = eventDateStr.toRequestBody("text/plain".toMediaTypeOrNull())
                    val startTimeRequestBody = startTimeStr.toRequestBody("text/plain".toMediaTypeOrNull())
                    val endTimeRequestBody = endTimeStr.toRequestBody("text/plain".toMediaTypeOrNull())
                    val locationRequestBody = location.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                    val costRequestBody = cost?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

                    val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
                        try {
                            val inputStream = context.contentResolver.openInputStream(uri)
                            if (inputStream == null) {
                                Log.e(TAG, "Could not open input stream for URI: $uri")
                                return@let null
                            }

                            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                            Log.d(TAG, "Image MIME type: $mimeType")

                            val extension = when {
                                mimeType.contains("png") -> "png"
                                mimeType.contains("jpg") || mimeType.contains("jpeg") -> "jpg"
                                mimeType.contains("webp") -> "webp"
                                else -> "jpg"
                            }

                            val fileName = "event_${System.currentTimeMillis()}.$extension"
                            val tempFile = File(context.cacheDir, fileName)

                            inputStream.use { input ->
                                FileOutputStream(tempFile).use { output ->
                                    input.copyTo(output)
                                }
                            }

                            if (!tempFile.exists() || tempFile.length() == 0L) {
                                Log.e(TAG, "Temp file creation failed or file is empty")
                                return@let null
                            }

                            Log.d(TAG, "Created temp file: ${tempFile.absolutePath}, size: ${tempFile.length()} bytes")

                            val requestFile = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("image", fileName, requestFile)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error preparing image file", e)
                            null
                        }
                    }

                    Log.d(TAG, "Making API call with image part: ${imagePart != null}")
                    Log.d(TAG, "Request data - Name: $name, Location: $location, Date: $eventDateStr")

                    val response = if (imagePart != null) {
                        Log.d(TAG, "Calling createEventWithImage API")
                        eventApi.createEventWithImage(
                            token = "Bearer $token",
                            name = nameRequestBody,
                            description = descriptionRequestBody,
                            eventDate = eventDateRequestBody,
                            startTime = startTimeRequestBody,
                            endTime = endTimeRequestBody,
                            location = locationRequestBody,
                            cost = costRequestBody,
                            image = imagePart
                        )
                    } else {
                        Log.d(TAG, "Calling regular createEvent API")
                        val createRequest = CreateEventRequest(
                            name = name.trim(),
                            description = description?.trim(),
                            eventDate = eventDateStr,
                            startTime = startTimeStr,
                            endTime = endTimeStr,
                            location = location.trim(),
                            image = null,
                            cost = cost
                        )
                        eventApi.createEvent("Bearer $token", createRequest)
                    }

                    Log.d(TAG, "API Response code: ${response.code()}")
                    Log.d(TAG, "API Response message: ${response.message()}")

                    if (!response.isSuccessful) {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "API Error response: $errorBody")
                        return@withContext Resource.Error("Server error: ${response.message()}")
                    }

                    val responseBody = response.body()
                    Log.d(TAG, "Response body success: ${responseBody?.success}")
                    Log.d(TAG, "Response body message: ${responseBody?.message}")

                    if (responseBody?.success == true) {
                        val createdEvent = responseBody.data
                        if (createdEvent != null) {
                            eventDao.insertEvent(createdEvent.toEntity().copy(isSynced = true))
                            return@withContext Resource.Success(createdEvent)
                        } else {
                            Log.e(TAG, "Created event data is null")
                            return@withContext Resource.Error("Event created but no data returned")
                        }
                    } else {
                        val errorMessage = responseBody?.message ?: "Unknown server error"
                        Log.e(TAG, "Server returned error: $errorMessage")
                        return@withContext Resource.Error(errorMessage)
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Network error during create event", e)
                    return@withContext Resource.Error("Network error: ${e.message}")
                }
            }

            val tempEventId = "temp_${UUID.randomUUID()}"
            val tempEvent = EventEntity(
                eventId = tempEventId,
                name = name.trim(),
                organizerUid = "",
                organizerName = null,
                organizerUsername = null,
                description = description?.trim(),
                eventDate = eventDateStr,
                startTime = startTimeStr,
                endTime = endTimeStr,
                location = location.trim(),
                image = null,
                cost = cost,
                participantCount = 0,
                createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date()),
                isSynced = false
            )

            eventDao.insertEvent(tempEvent)

            val createRequest = CreateEventRequest(
                name = name.trim(),
                description = description?.trim(),
                eventDate = eventDateStr,
                startTime = startTimeStr,
                endTime = endTimeStr,
                location = location.trim(),
                image = null,
                cost = cost
            )

            val pendingOperation = PendingOperationEntity(
                eventId = tempEventId,
                operationType = OPERATION_CREATE,
                eventData = gson.toJson(createRequest)
            )
            pendingOperationDao.insertPendingOperation(pendingOperation)

            Resource.Success(tempEvent.toEventItem())
        } catch (e: Exception) {
            Log.e(TAG, "Error creating event", e)
            Resource.Error("Error creating event: ${e.message}")
        }
    }

    suspend fun deleteEvent(token: String, eventId: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            if (eventId.isBlank()) {
                return@withContext Resource.Error("Event ID is required")
            }

            val localEvent = eventDao.getEventById(eventId)
            val isLocalUnsyncedEvent = localEvent?.isSynced == false

            if (isNetworkAvailable() && !isLocalUnsyncedEvent) {
                try {
                    val response = eventApi.deleteEvent("Bearer $token", eventId)
                    if (response.isSuccessful && response.body()?.success == true) {
                        eventDao.deleteEventPermanently(eventId)
                        return@withContext Resource.Success(Unit)
                    } else {
                        val errorMsg = response.body()?.message ?: "Failed to delete event"
                        return@withContext Resource.Error(errorMsg)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Network error during delete, falling back to offline", e)
                }
            }

            if (isLocalUnsyncedEvent) {
                eventDao.deleteEventPermanently(eventId)
                val pendingOps = pendingOperationDao.getAllPendingOperations()
                pendingOps.filter { it.eventId == eventId && it.operationType == OPERATION_CREATE }
                    .forEach { pendingOperationDao.deletePendingOperation(it.id) }
            } else {
                eventDao.markEventAsDeleted(eventId)
                val pendingOperation = PendingOperationEntity(
                    eventId = eventId,
                    operationType = OPERATION_DELETE,
                    eventData = null
                )
                pendingOperationDao.insertPendingOperation(pendingOperation)
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting event", e)
            Resource.Error("Error deleting event: ${e.message}")
        }
    }

    suspend fun syncPendingOperations(token: String) = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable()) return@withContext

        try {
            val pendingOperations = pendingOperationDao.getAllPendingOperations()
            Log.d(TAG, "Syncing ${pendingOperations.size} pending operations")

            for (operation in pendingOperations) {
                try {
                    when (operation.operationType) {
                        OPERATION_CREATE -> {
                            val createRequest = gson.fromJson(operation.eventData, CreateEventRequest::class.java)
                            val response = eventApi.createEvent("Bearer $token", createRequest)

                            if (response.isSuccessful && response.body()?.success == true) {
                                val createdEvent = response.body()?.data
                                if (createdEvent != null) {
                                    operation.eventId?.let { tempId ->
                                        eventDao.deleteEventPermanently(tempId)
                                    }

                                    eventDao.insertEvent(createdEvent.toEntity().copy(isSynced = true))

                                    pendingOperationDao.deletePendingOperation(operation.id)

                                    Log.d(TAG, "Successfully synced CREATE operation for event: ${createdEvent.eventId}")
                                }
                            } else {
                                Log.w(TAG, "Failed to sync CREATE operation: ${response.body()?.message}")
                            }
                        }

                        OPERATION_DELETE -> {
                            val response = eventApi.deleteEvent("Bearer $token", operation.eventId!!)

                            if (response.isSuccessful && response.body()?.success == true) {
                                eventDao.deleteEventPermanently(operation.eventId)

                                pendingOperationDao.deletePendingOperation(operation.id)

                                Log.d(TAG, "Successfully synced DELETE operation for event: ${operation.eventId}")
                            } else {
                                Log.w(TAG, "Failed to sync DELETE operation: ${response.body()?.message}")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error syncing operation ${operation.id}: ${e.message}", e)
                }
            }

            eventDao.cleanupSyncedDeletedEvents()

        } catch (e: Exception) {
            Log.e(TAG, "Error during sync: ${e.message}", e)
        }
    }

    suspend fun forceSync(token: String) {
        syncPendingOperations(token)
    }

    fun getOfflineEvents(): Flow<List<EventItem>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toEventItem() }
        }
    }

    suspend fun hasPendingOperations(): Boolean {
        return pendingOperationDao.getAllPendingOperations().isNotEmpty()
    }
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val fromCache: Boolean = false
) {
    class Success<T>(data: T, fromCache: Boolean = false) : Resource<T>(data, fromCache = fromCache)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}