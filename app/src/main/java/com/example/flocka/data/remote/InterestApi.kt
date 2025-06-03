package com.example.flocka.data.remote

import com.example.flocka.data.model.BulkInterestRequest
import com.example.flocka.data.model.InterestResponse
import retrofit2.Response
import retrofit2.http.*

interface InterestApi {

    @GET("api/interests/search")
    suspend fun searchInterests(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): Response<InterestResponse>

    @POST("api/user-interests/bulk-add")
    suspend fun bulkAddUserInterests(
        @Header("Authorization") token: String,
        @Body request: BulkInterestRequest
    ): Response<Unit>
}