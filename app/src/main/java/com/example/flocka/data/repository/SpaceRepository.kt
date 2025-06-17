package com.example.flocka.data.repository

import android.util.Log
import com.example.flocka.data.local.dao.SpaceDao
import com.example.flocka.data.local.entity.SpaceEntity
import com.example.flocka.data.model.SpaceItem
import com.example.flocka.data.remote.SpaceApi
import java.io.IOException

class SpaceRepository(
    private val spaceApi: SpaceApi,
    private val spaceDao: SpaceDao
) {
    suspend fun getSpaces(token: String): Result<List<SpaceItem>> {
        return try {
            val response = spaceApi.getSpaces("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val spaces = response.body()?.data ?: emptyList()

                val spaceEntities = spaces.map { SpaceEntity.fromSpaceItem(it) }
                spaceDao.insertSpaces(spaceEntities)

                Result.success(spaces)
            } else {
                val cachedSpaces = spaceDao.getAllSpaces().map { it.toSpaceItem() }
                if (cachedSpaces.isNotEmpty()) {
                    Log.d("SpaceRepository", "Using cached spaces")
                    Result.success(cachedSpaces)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Failed to fetch spaces"))
                }
            }
        } catch (e: IOException) {
            val cachedSpaces = spaceDao.getAllSpaces().map { it.toSpaceItem() }
            if (cachedSpaces.isNotEmpty()) {
                Log.d("SpaceRepository", "Network error - using cached spaces")
                Result.success(cachedSpaces)
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e("SpaceRepository", "Error fetching spaces", e)
            Result.failure(e)
        }
    }

    suspend fun getSpaceById(token: String, spaceId: String): Result<SpaceItem> {
        return try {
            val response = spaceApi.getSpaceById("Bearer $token", spaceId)
            if (response.isSuccessful && response.body()?.success == true) {
                val space = response.body()?.data
                if (space != null) {
                    spaceDao.insertSpace(SpaceEntity.fromSpaceItem(space))
                    Result.success(space)
                } else {
                    Result.failure(Exception("Space not found"))
                }
            } else {
                val cachedSpace = spaceDao.getSpaceById(spaceId)?.toSpaceItem()
                if (cachedSpace != null) {
                    Log.d("SpaceRepository", "Using cached space")
                    Result.success(cachedSpace)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Failed to fetch space details"))
                }
            }
        } catch (e: IOException) {
            val cachedSpace = spaceDao.getSpaceById(spaceId)?.toSpaceItem()
            if (cachedSpace != null) {
                Log.d("SpaceRepository", "Network error - using cached space")
                Result.success(cachedSpace)
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e("SpaceRepository", "Error fetching space details", e)
            Result.failure(e)
        }
    }

    suspend fun getCachedSpaces(): Result<List<SpaceItem>> {
        return try {
            val cachedSpaces = spaceDao.getAllSpaces().map { it.toSpaceItem() }
            Result.success(cachedSpaces)
        } catch (e: Exception) {
            Log.e("SpaceRepository", "Error getting cached spaces", e)
            Result.failure(e)
        }
    }

    suspend fun clearOldSpaces(cutoffTime: Long) {
        try {
            spaceDao.deleteOldSpaces(cutoffTime)
        } catch (e: Exception) {
            Log.e("SpaceRepository", "Error clearing old spaces", e)
        }
    }
}