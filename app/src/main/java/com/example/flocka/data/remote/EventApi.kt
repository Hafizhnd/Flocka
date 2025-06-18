package com.example.flocka.data.remote

import com.example.flocka.data.model.CreateEventRequest
import com.example.flocka.data.model.EventItem
import com.example.flocka.data.model.EventSuccessResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("api/events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body eventRequest: CreateEventRequest
    ): Response<EventSuccessResponse<EventItem>>

    @Multipart
    @POST("api/events")
    suspend fun createEventWithImage(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("event_date") eventDate: RequestBody,
        @Part("start_time") startTime: RequestBody,
        @Part("end_time") endTime: RequestBody,
        @Part("location") location: RequestBody,
        @Part("cost") cost: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<EventSuccessResponse<EventItem>>

    @DELETE("api/events/{eventId}")
    suspend fun deleteEvent(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): Response<EventSuccessResponse<Unit>>
}