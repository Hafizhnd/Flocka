package com.example.flocka.data.local.dao

import androidx.room.*
import com.example.flocka.data.local.entity.CommunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {
    @Query("SELECT * FROM communities")
    fun getAllCommunities(): Flow<List<CommunityEntity>>

    @Query("SELECT * FROM communities WHERE communityId = :communityId")
    suspend fun getCommunityById(communityId: String): CommunityEntity?

    @Query("SELECT * FROM communities WHERE isSynced = 0")
    suspend fun getUnsyncedCommunities(): List<CommunityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunity(community: CommunityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunities(communities: List<CommunityEntity>)

    @Update
    suspend fun updateCommunity(community: CommunityEntity)

    @Delete
    suspend fun deleteCommunity(community: CommunityEntity)

    @Query("DELETE FROM communities WHERE communityId = :communityId")
    suspend fun deleteCommunity(communityId: String)

    @Query("UPDATE communities SET isSynced = 1 WHERE communityId = :communityId")
    suspend fun markAsSynced(communityId: String)

    @Query("DELETE FROM communities")
    suspend fun deleteAllCommunities()
} 