package com.example.flocka.data.remote

import com.example.flocka.data.model.SpaceItem
import com.example.flocka.data.model.EventSuccessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpaceApi {
    @GET("api/spaces")
    suspend fun getSpaces(
        @Header("Authorization") token: String
    ): Response<EventSuccessResponse<List<SpaceItem>>>

    @GET("api/spaces/{spaceId}")
    suspend fun getSpaceById(
        @Header("Authorization") token: String,
        @Path("spaceId") spaceId: String
    ): Response<EventSuccessResponse<SpaceItem>>
}