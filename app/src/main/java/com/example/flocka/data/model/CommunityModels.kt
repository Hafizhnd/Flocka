package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class CommunityItem(
    @SerializedName("community_id") val communityId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("created_by") val createdByUid: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("image") val image: String?,

    @SerializedName("creator_name") val creatorName: String?,
    @SerializedName("creator_username") val creatorUsername: String?,
    @SerializedName("member_count") val memberCount: Int?
)

data class CreateCommunityRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String? = null
)

data class UpdateCommunityRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("image") val image: String? = null
)

typealias CommunitySuccessResponse<T> = EventSuccessResponse<T>
