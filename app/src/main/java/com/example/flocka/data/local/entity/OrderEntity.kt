package com.example.flocka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flocka.data.model.OrderItem

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val userId: String,
    val itemType: String,
    val itemId: String,
    val itemName: String,
    val itemImage: String?,
    val bookedStartDatetime: String,
    val bookedEndDatetime: String?,
    val quantity: Int,
    val amountPaid: Double,
    val currency: String?,
    val orderStatus: String,
    val createdAt: String,
    val fetchedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = true
) {
    fun toOrderItem(): OrderItem {
        return OrderItem(
            orderId = orderId,
            userId = userId,
            itemType = itemType,
            itemId = itemId,
            itemName = itemName,
            itemImage = itemImage,
            bookedStartDatetime = bookedStartDatetime,
            bookedEndDatetime = bookedEndDatetime,
            quantity = quantity,
            amountPaid = amountPaid,
            currency = currency,
            orderStatus = orderStatus,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromOrderItem(orderItem: OrderItem, isSynced: Boolean = true): OrderEntity {
            return OrderEntity(
                orderId = orderItem.orderId,
                userId = orderItem.userId,
                itemType = orderItem.itemType,
                itemId = orderItem.itemId,
                itemName = orderItem.itemName,
                itemImage = orderItem.itemImage,
                bookedStartDatetime = orderItem.bookedStartDatetime,
                bookedEndDatetime = orderItem.bookedEndDatetime,
                quantity = orderItem.quantity,
                amountPaid = orderItem.amountPaid,
                currency = orderItem.currency,
                orderStatus = orderItem.orderStatus,
                createdAt = orderItem.createdAt,
                isSynced = isSynced
            )
        }
    }
}