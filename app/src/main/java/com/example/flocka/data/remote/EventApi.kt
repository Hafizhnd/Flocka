package com.example.flocka.data.remote

import com.example.flocka.data.model.EventItem
import com.example.flocka.data.model.EventSuccessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface EventApi {

    @GET("api/events")
    suspend fun getEvents(
        @Header("Authorization") token: String
    ): Response<EventSuccessResponse<List<EventItem>>>

    @GET("api/events/{eventId}")
    suspend fun getEventById(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): Response<EventSuccessResponse<EventItem>>

    @GET("api/events/upcoming")
    suspend fun getUpcomingEvents(
        @Header("Authorization") token: String
    ): Response<EventSuccessResponse<List<EventItem>>>
}