package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class Interest(
    @SerializedName("interest_id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class InterestResponse(
    val success: Boolean,
    val data: List<Interest>
)

data class BulkInterestRequest(
    val interestIds: List<String>
)