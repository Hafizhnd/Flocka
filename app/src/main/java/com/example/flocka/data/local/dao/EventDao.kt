package com.example.flocka.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flocka.data.local.entity.EventEntity
import com.example.flocka.data.local.entity.PendingOperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE isDeleted = 0 ORDER BY lastModified DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isDeleted = 0 ORDER BY lastModified DESC")
    suspend fun getAllEventsSync(): List<EventEntity>

    @Query("SELECT * FROM events WHERE eventId = :eventId AND isDeleted = 0")
    suspend fun getEventById(eventId: String): EventEntity?

    @Query("SELECT * FROM events WHERE eventId = :eventId AND isDeleted = 0")
    fun getEventByIdFlow(eventId: String): Flow<EventEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("UPDATE events SET isDeleted = 1, lastModified = :timestamp WHERE eventId = :eventId")
    suspend fun markEventAsDeleted(eventId: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM events WHERE eventId = :eventId")
    suspend fun deleteEventPermanently(eventId: String)

    @Query("SELECT * FROM events WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE isSynced = 0 AND isDeleted = 1")
    suspend fun getUnsyncedDeletedEvents(): List<EventEntity>

    @Query("UPDATE events SET isSynced = 1 WHERE eventId = :eventId")
    suspend fun markEventAsSynced(eventId: String)

    @Query("DELETE FROM events WHERE isDeleted = 1 AND isSynced = 1")
    suspend fun cleanupSyncedDeletedEvents()
}

@Dao
interface PendingOperationDao {
    @Query("SELECT * FROM pending_operations ORDER BY createdAt ASC")
    suspend fun getAllPendingOperations(): List<PendingOperationEntity>

    @Insert
    suspend fun insertPendingOperation(operation: PendingOperationEntity)

    @Query("DELETE FROM pending_operations WHERE id = :operationId")
    suspend fun deletePendingOperation(operationId: Long)

    @Query("DELETE FROM pending_operations")
    suspend fun deleteAllPendingOperations()
}