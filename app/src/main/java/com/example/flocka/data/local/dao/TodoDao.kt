package com.example.flocka.data.local.dao

import androidx.room.*
import com.example.flocka.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE isDeleted = 0 ORDER BY fetchedAt DESC")
    suspend fun getAllTodos(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE todoId = :todoId AND isDeleted = 0")
    suspend fun getTodoById(todoId: String): TodoEntity?

    @Query("SELECT * FROM todos WHERE isDeleted = 0 ORDER BY fetchedAt DESC")
    fun getAllTodosFlow(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<TodoEntity>)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE todoId = :todoId")
    suspend fun deleteTodoById(todoId: String)

    @Query("UPDATE todos SET isDone = :isDone, isSynced = :isSynced, syncOperation = :syncOperation WHERE todoId = :todoId")
    suspend fun toggleTodoStatus(todoId: String, isDone: Boolean, isSynced: Boolean = false, syncOperation: String? = "UPDATE")

    @Query("DELETE FROM todos WHERE fetchedAt < :cutoffTime")
    suspend fun deleteOldTodos(cutoffTime: Long)

    @Query("DELETE FROM todos")
    suspend fun deleteAllTodos()

    @Query("SELECT * FROM todos WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedTodos(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE syncOperation = 'CREATE' AND isSynced = 0")
    suspend fun getTodosToCreate(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE syncOperation = 'UPDATE' AND isSynced = 0")
    suspend fun getTodosToUpdate(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE syncOperation = 'DELETE' AND isSynced = 0")
    suspend fun getTodosToDelete(): List<TodoEntity>

    @Query("UPDATE todos SET isSynced = 1, syncOperation = NULL WHERE todoId = :todoId")
    suspend fun markTodoAsSynced(todoId: String)

    @Query("UPDATE todos SET isDeleted = 1, isSynced = 0, syncOperation = 'DELETE' WHERE todoId = :todoId")
    suspend fun softDeleteTodo(todoId: String)

    @Query("DELETE FROM todos WHERE todoId = :todoId AND syncOperation = 'DELETE' AND isSynced = 1")
    suspend fun hardDeleteSyncedTodo(todoId: String)

    @Query("DELETE FROM todos WHERE isSynced = 1")
    suspend fun clearSyncedTodos()
}