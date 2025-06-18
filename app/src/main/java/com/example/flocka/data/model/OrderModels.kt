package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("order_id") val orderId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("item_type") val itemType: String,
    @SerializedName("item_id") val itemId: String,
    @SerializedName("item_name") val itemName: String,
    @SerializedName("item_image") val itemImage: String?,
    @SerializedName("booked_start_datetime") val bookedStartDatetime: String,
    @SerializedName("booked_end_datetime") val bookedEndDatetime: String?,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("amount_paid") val amountPaid: Double,
    @SerializedName("currency") val currency: String?,
    @SerializedName("order_status") val orderStatus: String,
    @SerializedName("created_at") val createdAt: String
)

data class CreateOrderRequest(
    @SerializedName("item_type") val itemType: String,
    @SerializedName("item_id") val itemId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("amount_paid") val amountPaid: Double,
    @SerializedName("currency") val currency: String? = "IDR",
    @SerializedName("booked_start_datetime") val bookedStartDatetime: String,
    @SerializedName("booked_end_datetime") val bookedEndDatetime: String? = null
)

data class OrderSuccessResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String? = null
)

data class OrderDetails(
    val email: String,
    val name: String,
    val phone: String,
    val type: String,
    val duration: Int,
    val date: String,
    val promoCode: String
)