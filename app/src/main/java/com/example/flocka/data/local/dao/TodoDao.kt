package com.example.flocka.data.local.dao

import androidx.room.*
import com.example.flocka.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY fetchedAt DESC")
    suspend fun getAllTodos(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE todoId = :todoId")
    suspend fun getTodoById(todoId: String): TodoEntity?

    @Query("SELECT * FROM todos ORDER BY fetchedAt DESC")
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

    @Query("UPDATE todos SET isDone = :isDone WHERE todoId = :todoId")
    suspend fun toggleTodoStatus(todoId: String, isDone: Boolean)

    @Query("DELETE FROM todos WHERE fetchedAt < :cutoffTime")
    suspend fun deleteOldTodos(cutoffTime: Long)

    @Query("DELETE FROM todos")
    suspend fun deleteAllTodos()

    @Query("SELECT * FROM todos WHERE isLocalOnly = 1")
    suspend fun getLocalOnlyTodos(): List<TodoEntity>

    @Query("UPDATE todos SET isLocalOnly = 0 WHERE todoId = :todoId")
    suspend fun markTodoAsSynced(todoId: String)
}