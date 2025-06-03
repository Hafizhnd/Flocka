package com.example.flocka.data.remote

import com.example.flocka.data.model.CommunityItem
import com.example.flocka.data.model.CreateCommunityRequest
import com.example.flocka.data.model.UpdateCommunityRequest
import com.example.flocka.data.model.CommunitySuccessResponse
import retrofit2.Response
import retrofit2.http.*

interface CommunityApi {

    @POST("api/communities")
    suspend fun createCommunity(
        @Header("Authorization") token: String,
        @Body request: CreateCommunityRequest
    ): Response<CommunitySuccessResponse<CommunityItem>>

    @GET("api/communities")
    suspend fun getCommunities(
        @Header("Authorization") token: String
    ): Response<CommunitySuccessResponse<List<CommunityItem>>>

    @GET("api/communities/my-communities")
    suspend fun getMyCommunities(
        @Header("Authorization") token: String
    ): Response<CommunitySuccessResponse<List<CommunityItem>>>

    @GET("api/communities/{communityId}")
    suspend fun getCommunityById(
        @Header("Authorization") token: String,
        @Path("communityId") communityId: String
    ): Response<CommunitySuccessResponse<CommunityItem>>

    @PUT("api/communities/{communityId}")
    suspend fun updateCommunity(
        @Header("Authorization") token: String,
        @Path("communityId") communityId: String,
        @Body request: UpdateCommunityRequest
    ): Response<CommunitySuccessResponse<CommunityItem>>

    @DELETE("api/communities/{communityId}")
    suspend fun deleteCommunity(
        @Header("Authorization") token: String,
        @Path("communityId") communityId: String
    ): Response<CommunitySuccessResponse<Unit>>

    @POST("api/communities/{communityId}/join")
    suspend fun joinCommunity(
        @Header("Authorization") token: String,
        @Path("communityId") communityId: String
    ): Response<CommunitySuccessResponse<Unit>>

    @POST("api/communities/{communityId}/leave")
    suspend fun leaveCommunity(
        @Header("Authorization") token: String,
        @Path("communityId") communityId: String
    ): Response<CommunitySuccessResponse<Unit>>
}