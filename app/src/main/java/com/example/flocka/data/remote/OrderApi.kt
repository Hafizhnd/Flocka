package com.example.flocka.data.api

import com.example.flocka.data.model.CreateOrderRequest
import com.example.flocka.data.model.OrderItem
import com.example.flocka.data.model.OrderSuccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApi {

    @POST("api/orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: CreateOrderRequest
    ): Response<OrderSuccessResponse<OrderItem>>

    @GET("api/orders/my")
    suspend fun getMyOrders(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): Response<OrderSuccessResponse<List<OrderItem>>>

}