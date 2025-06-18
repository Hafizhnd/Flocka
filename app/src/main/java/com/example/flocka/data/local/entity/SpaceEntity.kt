package com.example.flocka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flocka.data.model.SpaceItem

@Entity(tableName = "spaces")
data class SpaceEntity(
    @PrimaryKey
    val spaceId: String,
    val name: String,
    val location: String,
    val description: String?,
    val costPerHour: Double?,
    val openingTime: String?,
    val closingTime: String?,
    val image: String?,
    val createdAt: String?,
    val fetchedAt: Long = System.currentTimeMillis(),
    val isLocalOnly: Boolean = false
) {
    fun toSpaceItem(): SpaceItem {
        return SpaceItem(
            spaceId = spaceId,
            name = name,
            location = location,
            description = description,
            costPerHour = costPerHour,
            openingTime = openingTime,
            closingTime = closingTime,
            image = image,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromSpaceItem(spaceItem: SpaceItem): SpaceEntity {
            return SpaceEntity(
                spaceId = spaceItem.spaceId,
                name = spaceItem.name,
                location = spaceItem.location,
                description = spaceItem.description,
                costPerHour = spaceItem.costPerHour,
                openingTime = spaceItem.openingTime,
                closingTime = spaceItem.closingTime,
                image = spaceItem.image,
                createdAt = spaceItem.createdAt
            )
        }
    }
}