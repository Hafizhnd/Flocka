package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class SpaceItem(
    @SerializedName("space_id") val spaceId: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: String,
    @SerializedName("description") val description: String?,
    @SerializedName("cost_per_hour") val costPerHour: Double?,
    @SerializedName("opening_time") val openingTime: String?,
    @SerializedName("closing_time") val closingTime: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("created_at") val createdAt: String?
)