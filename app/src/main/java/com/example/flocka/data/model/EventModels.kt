package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class EventItem(
    @SerializedName("event_id") val eventId: String,
    @SerializedName("name") val name: String,
    @SerializedName("organizer") val organizerUid: String,
    @SerializedName("organizer_name") val organizerName: String?,
    @SerializedName("organizer_username") val organizerUsername: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("event_date") val eventDate: String?,
    @SerializedName("start_time") val startTime: String, // Assuming this is always present

    // --- MODIFIED TO BE NULLABLE ---
    @SerializedName("end_time") val endTime: String?,    // Changed to String?
    @SerializedName("location") val location: String?,  // Changed to String?

    @SerializedName("image") val image: String?,
    @SerializedName("cost") val cost: Double?,
    @SerializedName("participant_count") val participantCount: Int?,

    // --- MODIFIED TO BE NULLABLE ---
    @SerializedName("created_at") val createdAt: String? // Changed to String?
)


data class EventSuccessResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String? = null
)

data class CreateEventRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("event_date") val eventDate: String, // Format: YYYY-MM-DD
    @SerializedName("start_time") val startTime: String, // ISO date string or your preferred format
    @SerializedName("end_time") val endTime: String,     // ISO date string or your preferred format
    @SerializedName("location") val location: String,
    @SerializedName("image") val image: String?,
    @SerializedName("cost") val cost: Double?
)