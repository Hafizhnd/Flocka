package com.example.flocka.data.local.dao

import androidx.room.*
import com.example.flocka.data.local.entity.SpaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceDao {
    @Query("SELECT * FROM spaces ORDER BY fetchedAt DESC")
    suspend fun getAllSpaces(): List<SpaceEntity>

    @Query("SELECT * FROM spaces WHERE spaceId = :spaceId")
    suspend fun getSpaceById(spaceId: String): SpaceEntity?

    @Query("SELECT * FROM spaces ORDER BY fetchedAt DESC")
    fun getAllSpacesFlow(): Flow<List<SpaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpace(space: SpaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpaces(spaces: List<SpaceEntity>)

    @Update
    suspend fun updateSpace(space: SpaceEntity)

    @Delete
    suspend fun deleteSpace(space: SpaceEntity)

    @Query("DELETE FROM spaces WHERE spaceId = :spaceId")
    suspend fun deleteSpaceById(spaceId: String)

    @Query("DELETE FROM spaces WHERE fetchedAt < :cutoffTime")
    suspend fun deleteOldSpaces(cutoffTime: Long)

    @Query("DELETE FROM spaces")
    suspend fun deleteAllSpaces()

    @Query("SELECT * FROM spaces WHERE isLocalOnly = 1")
    suspend fun getLocalOnlySpaces(): List<SpaceEntity>

    @Query("UPDATE spaces SET isLocalOnly = 0 WHERE spaceId = :spaceId")
    suspend fun markSpaceAsSynced(spaceId: String)
}