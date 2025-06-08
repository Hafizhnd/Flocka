package com.example.flocka.data.repository

import android.util.Log
import com.example.flocka.data.local.dao.CommunityDao
import com.example.flocka.data.local.entity.CommunityEntity
import com.example.flocka.data.model.CommunityItem
import com.example.flocka.data.model.CreateCommunityRequest
import com.example.flocka.data.model.UpdateCommunityRequest
import com.example.flocka.data.remote.CommunityApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class CommunityRepository(
    private val communityApi: CommunityApi,
    private val communityDao: CommunityDao
) {
    fun getAllCommunities(): Flow<List<CommunityItem>> {
        return communityDao.getAllCommunities().map { entities ->
            entities.map { it.toCommunityItem() }
        }
    }

    suspend fun refreshCommunities(token: String) {
        try {
            val response = communityApi.getCommunities("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val communities = response.body()?.data ?: emptyList()
                communityDao.insertCommunities(
                    communities.map { CommunityEntity.fromCommunityItem(it) }
                )
            }
        } catch (e: IOException) {
            Log.e("CommunityRepo", "Error refreshing communities", e)
            // If offline, we'll use cached data from Room
        }
    }

    suspend fun getCommunityById(token: String, communityId: String): CommunityItem? {
        return try {
            val response = communityApi.getCommunityById("Bearer $token", communityId)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.also { community ->
                    communityDao.insertCommunity(CommunityEntity.fromCommunityItem(community))
                }
            } else {
                communityDao.getCommunityById(communityId)?.toCommunityItem()
            }
        } catch (e: IOException) {
            communityDao.getCommunityById(communityId)?.toCommunityItem()
        }
    }

    suspend fun createCommunity(
        token: String,
        name: String,
        description: String?,
        image: String? = null
    ): Result<CommunityItem> {
        return try {
            val request = CreateCommunityRequest(name, description, image)
            val response = communityApi.createCommunity("Bearer $token", request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val community = response.body()?.data
                if (community != null) {
                    communityDao.insertCommunity(CommunityEntity.fromCommunityItem(community))
                    Result.success(community)
                } else {
                    Result.failure(Exception("Community created but no data returned"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create community"))
            }
        } catch (e: IOException) {
            // Store locally with isSynced = false when offline
            val tempId = "temp_${System.currentTimeMillis()}"
            val tempCommunity = CommunityItem(
                communityId = tempId,
                name = name,
                description = description,
                createdByUid = "", // Will be set when synced
                createdAt = System.currentTimeMillis().toString(),
                image = image,
                creatorName = null,
                creatorUsername = null,
                memberCount = 0
            )
            communityDao.insertCommunity(CommunityEntity.fromCommunityItem(tempCommunity, isSynced = false))
            Result.success(tempCommunity)
        }
    }

    suspend fun updateCommunity(
        token: String,
        communityId: String,
        name: String?,
        description: String?,
        image: String? = null
    ): Result<CommunityItem> {
        return try {
            val request = UpdateCommunityRequest(name, description, image)
            val response = communityApi.updateCommunity("Bearer $token", communityId, request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val community = response.body()?.data
                if (community != null) {
                    communityDao.insertCommunity(CommunityEntity.fromCommunityItem(community))
                    Result.success(community)
                } else {
                    Result.failure(Exception("Community updated but no data returned"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to update community"))
            }
        } catch (e: IOException) {
            // Update locally with isSynced = false when offline
            val currentCommunity = communityDao.getCommunityById(communityId)
            if (currentCommunity != null) {
                val updatedCommunity = currentCommunity.copy(
                    name = name ?: currentCommunity.name,
                    description = description ?: currentCommunity.description,
                    image = image ?: currentCommunity.image,
                    isSynced = false
                )
                communityDao.updateCommunity(updatedCommunity)
                Result.success(updatedCommunity.toCommunityItem())
            } else {
                Result.failure(Exception("Community not found locally"))
            }
        }
    }

    suspend fun syncUnsyncedCommunities(token: String) {
        val unsyncedCommunities = communityDao.getUnsyncedCommunities()
        for (community in unsyncedCommunities) {
            try {
                // Check if this is a temporary ID (new community)
                if (community.communityId.startsWith("temp_")) {
                    // This is a new community, create it
                    val createRequest = CreateCommunityRequest(
                        name = community.name,
                        description = community.description,
                        image = community.image
                    )
                    val response = communityApi.createCommunity(
                        "Bearer $token",
                        createRequest
                    )
                    if (response.isSuccessful && response.body()?.success == true) {
                        val newCommunity = response.body()?.data
                        if (newCommunity != null) {
                            // Delete the temporary community
                            communityDao.deleteCommunity(community.communityId)
                            // Insert the new one from server
                            communityDao.insertCommunity(CommunityEntity.fromCommunityItem(newCommunity))
                        }
                    }
                } else {
                    // This is an existing community, update it
                    val updateRequest = UpdateCommunityRequest(
                        name = community.name,
                        description = community.description,
                        image = community.image
                    )
                    val response = communityApi.updateCommunity(
                        "Bearer $token",
                        community.communityId,
                        updateRequest
                    )
                    if (response.isSuccessful && response.body()?.success == true) {
                        communityDao.markAsSynced(community.communityId)
                    }
                }
            } catch (e: Exception) {
                Log.e("CommunityRepo", "Error syncing community ${community.communityId}", e)
            }
        }
    }
} 