package com.example.flocka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flocka.data.model.CommunityItem

@Entity(tableName = "communities")
data class CommunityEntity(
    @PrimaryKey
    val communityId: String,
    val name: String,
    val description: String?,
    val createdByUid: String,
    val createdAt: String,
    val image: String?,
    val creatorName: String?,
    val creatorUsername: String?,
    val memberCount: Int?,
    val isSynced: Boolean = true
) {
    fun toCommunityItem(): CommunityItem {
        return CommunityItem(
            communityId = communityId,
            name = name,
            description = description,
            createdByUid = createdByUid,
            createdAt = createdAt,
            image = image,
            creatorName = creatorName,
            creatorUsername = creatorUsername,
            memberCount = memberCount
        )
    }

    companion object {
        fun fromCommunityItem(communityItem: CommunityItem, isSynced: Boolean = true): CommunityEntity {
            return CommunityEntity(
                communityId = communityItem.communityId,
                name = communityItem.name,
                description = communityItem.description,
                createdByUid = communityItem.createdByUid,
                createdAt = communityItem.createdAt,
                image = communityItem.image,
                creatorName = communityItem.creatorName,
                creatorUsername = communityItem.creatorUsername,
                memberCount = communityItem.memberCount,
                isSynced = isSynced
            )
        }
    }
} 