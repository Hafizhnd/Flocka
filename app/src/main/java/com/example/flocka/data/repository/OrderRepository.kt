package com.example.flocka.data.repository

import android.util.Log
import com.example.flocka.data.api.OrderApi
import com.example.flocka.data.local.dao.OrderDao
import com.example.flocka.data.local.entity.OrderEntity
import com.example.flocka.data.model.CreateOrderRequest
import com.example.flocka.data.model.OrderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class OrderRepository(
    private val orderApi: OrderApi,
    private val orderDao: OrderDao
) {

    fun getAllOrders(): Flow<List<OrderItem>> {
        return orderDao.getAllOrders().map { entities ->
            entities.map { it.toOrderItem() }
        }
    }

    suspend fun fetchMyOrders(token: String): Result<List<OrderItem>> {
        return try {
            syncUnsyncedOrders(token)

            val response = orderApi.getMyOrders("Bearer $token", null)
            if (response.isSuccessful && response.body()?.success == true) {
                val orders = response.body()?.data ?: emptyList()

                orderDao.deleteAllSyncedOrders()
                val orderEntities = orders.map { OrderEntity.fromOrderItem(it, isSynced = true) }
                orderDao.insertOrders(orderEntities)

                Result.success(orders)
            } else {
                Log.d("OrderRepository", "API failed, UI will show cached orders from Room")
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch orders"))
            }
        } catch (e: IOException) {
            Log.d("OrderRepository", "Network error - UI will show cached orders from Room")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching orders", e)
            Result.failure(e)
        }
    }


    suspend fun createOrder(
        token: String,
        request: CreateOrderRequest
    ): Result<OrderItem> {
        return try {
            val response = orderApi.createOrder("Bearer $token", request)
            if (response.isSuccessful && response.body()?.success == true) {
                val order = response.body()?.data
                if (order != null) {
                    orderDao.insertOrder(OrderEntity.fromOrderItem(order, isSynced = true))
                    Result.success(order)
                } else {
                    Result.failure(Exception("No order data received"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create order"))
            }
        } catch (e: IOException) {
            Log.d("OrderRepository", "Offline - creating local order")
            val offlineOrder = createOfflineOrder(request)
            Result.success(offlineOrder)
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error creating order", e)
            Result.failure(e)
        }
    }

    private suspend fun createOfflineOrder(request: CreateOrderRequest): OrderItem {
        val tempOrderId = "offline_${System.currentTimeMillis()}"

        val offlineOrder = OrderItem(
            orderId = tempOrderId,
            userId = "",
            itemType = request.itemType,
            itemId = request.itemId,
            itemName = request.itemType,
            itemImage = null,
            bookedStartDatetime = request.bookedStartDatetime,
            bookedEndDatetime = request.bookedEndDatetime ?: request.bookedStartDatetime,
            quantity = request.quantity,
            amountPaid = request.amountPaid,
            currency = request.currency ?: "IDR",
            orderStatus = "PENDING_SYNC",
            createdAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US).format(java.util.Date())
        )

        orderDao.insertOrder(OrderEntity.fromOrderItem(offlineOrder, isSynced = false))

        return offlineOrder
    }

    private suspend fun syncUnsyncedOrders(token: String) {
        try {
            val unsyncedOrders = orderDao.getUnsyncedOrders()

            unsyncedOrders.forEach { orderEntity ->
                try {
                    if (!orderEntity.orderId.startsWith("offline_")) {
                        return@forEach
                    }

                    val request = CreateOrderRequest(
                        itemType = orderEntity.itemType,
                        itemId = orderEntity.itemId,
                        quantity = orderEntity.quantity,
                        amountPaid = orderEntity.amountPaid,
                        currency = orderEntity.currency,
                        bookedStartDatetime = orderEntity.bookedStartDatetime,
                        bookedEndDatetime = orderEntity.bookedEndDatetime
                    )

                    val response = orderApi.createOrder("Bearer $token", request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        val syncedOrder = response.body()?.data
                        if (syncedOrder != null) {
                            orderDao.deleteOrder(orderEntity)
                            orderDao.insertOrder(OrderEntity.fromOrderItem(syncedOrder, isSynced = true))

                            Log.d("OrderRepository", "Successfully synced offline order: ${orderEntity.orderId}")
                        }
                    } else {
                        Log.w("OrderRepository", "Failed to sync offline order: ${orderEntity.orderId}")
                    }
                } catch (e: Exception) {
                    Log.e("OrderRepository", "Error syncing offline order: ${orderEntity.orderId}", e)
                }
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error in syncUnsyncedOrders", e)
        }
    }

    suspend fun clearOldData() {
        val cutoffTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000) // 7 days
        orderDao.deleteOldOrders(cutoffTime)
    }
}