package com.example.flocka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flocka.data.model.EventItem

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val eventId: String,
    val name: String,
    val organizerUid: String?,
    val organizerName: String?,
    val organizerUsername: String?,
    val description: String?,
    val eventDate: String?,
    val startTime: String?,
    val endTime: String?,
    val location: String?,
    val image: String?,
    val cost: Double?,
    val participantCount: Int?,
    val createdAt: String?,

    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
)

@Entity(tableName = "pending_operations")
data class PendingOperationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: String?, // null for create operations
    val operationType: String, // "CREATE", "DELETE"
    val eventData: String?, // JSON string of event data for create operations
    val createdAt: Long = System.currentTimeMillis()
)

fun EventItem.toEntity(): EventEntity {
    return EventEntity(
        eventId = this.eventId,
        name = this.name,
        organizerUid = this.organizerUid,
        organizerName = this.organizerName,
        organizerUsername = this.organizerUsername,
        description = this.description,
        eventDate = this.eventDate,
        startTime = this.startTime,
        endTime = this.endTime,
        location = this.location,
        image = this.image,
        cost = this.cost,
        participantCount = this.participantCount,
        createdAt = this.createdAt,
        isSynced = true // Coming from API means it's synced
    )
}

fun EventEntity.toEventItem(): EventItem {
    return EventItem(
        eventId = this.eventId,
        name = this.name,
        organizerUid = this.organizerUid.toString(),
        organizerName = this.organizerName,
        organizerUsername = this.organizerUsername,
        description = this.description,
        eventDate = this.eventDate,
        startTime = this.startTime.toString(),
        endTime = this.endTime,
        location = this.location,
        image = this.image,
        cost = this.cost,
        participantCount = this.participantCount,
        createdAt = this.createdAt
    )
}