package com.example.flocka.data.remote

import com.example.flocka.data.model.BulkInterestRequest
import com.example.flocka.data.model.InterestResponse
import retrofit2.Response
import retrofit2.http.*

interface InterestApi {
    // Searches for interests based on user input
    @GET("api/interests/search") // This route must exist in your backend interest routes
    suspend fun searchInterests(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): Response<InterestResponse>

    // Saves the list of selected interests
    @POST("api/user-interests/bulk-add") // The new route we created for UserInterests
    suspend fun bulkAddUserInterests(
        @Header("Authorization") token: String,
        @Body request: BulkInterestRequest
    ): Response<Unit> // We only care if it succeeds or fails
}