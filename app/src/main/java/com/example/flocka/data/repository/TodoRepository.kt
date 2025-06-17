package com.example.flocka.data.repository

import android.util.Log
import com.example.flocka.data.api.TodoApi
import com.example.flocka.data.local.dao.TodoDao
import com.example.flocka.data.local.entity.TodoEntity
import com.example.flocka.data.model.CreateTodoRequest
import com.example.flocka.data.model.TodoItem
import com.example.flocka.data.model.UpdateTodoRequest
import java.io.IOException
import java.util.UUID

class TodoRepository(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {

    suspend fun getTodos(token: String): Result<List<TodoItem>> {
        return try {
            val response = todoApi.getTodos("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val todos = response.body()?.data ?: emptyList()

                val todoEntities = todos.map { TodoEntity.fromTodoItem(it) }
                todoDao.insertTodos(todoEntities)

                Result.success(todos)
            } else {
                val cachedTodos = todoDao.getAllTodos().map { it.toTodoItem() }
                if (cachedTodos.isNotEmpty()) {
                    Log.d("TodoRepository", "Using cached todos")
                    Result.success(cachedTodos)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Failed to fetch todos"))
                }
            }
        } catch (e: IOException) {
            val cachedTodos = todoDao.getAllTodos().map { it.toTodoItem() }
            if (cachedTodos.isNotEmpty()) {
                Log.d("TodoRepository", "Network error - using cached todos")
                Result.success(cachedTodos)
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error fetching todos", e)
            Result.failure(e)
        }
    }

    suspend fun createTodo(
        token: String,
        taskTitle: String,
        taskDescription: String?,
        startTime: String?,
        endTime: String?,
        date: String?
    ): Result<String> {
        return try {
            val request = CreateTodoRequest(taskTitle, taskDescription, startTime, endTime, date)
            val response = todoApi.createTodo("Bearer $token", request)

            if (response.isSuccessful && response.body()?.success == true) {
                val createdTodo = response.body()?.data
                if (createdTodo != null) {
                    todoDao.insertTodo(TodoEntity.fromTodoItem(createdTodo))
                }
                Result.success(response.body()?.message ?: "Task added!")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to add task"))
            }
        } catch (e: IOException) {
            val localTodo = TodoEntity(
                todoId = UUID.randomUUID().toString(),
                userId = "",
                taskTitle = taskTitle,
                taskDescription = taskDescription,
                startTime = startTime,
                endTime = endTime,
                date = date,
                isDone = false,
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
                isLocalOnly = true
            )
            todoDao.insertTodo(localTodo)

            Log.d("TodoRepository", "Offline - storing todo for later sync")
            Result.failure(Exception("Offline - todo will be synced later"))
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error creating todo", e)
            Result.failure(e)
        }
    }

    suspend fun updateTodo(
        token: String,
        todoId: String,
        taskTitle: String?,
        taskDescription: String?,
        startTime: String?,
        endTime: String?,
        date: String?
    ): Result<String> {
        return try {
            val request = UpdateTodoRequest(taskTitle, taskDescription, startTime, endTime, date)
            val response = todoApi.updateTodo("Bearer $token", todoId, request)

            if (response.isSuccessful && response.body()?.success == true) {
                val updatedTodo = response.body()?.data
                if (updatedTodo != null) {
                    todoDao.insertTodo(TodoEntity.fromTodoItem(updatedTodo))
                }
                Result.success(response.body()?.message ?: "Task updated!")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to update task"))
            }
        } catch (e: IOException) {
            val existingTodo = todoDao.getTodoById(todoId)
            if (existingTodo != null) {
                val updatedTodo = existingTodo.copy(
                    taskTitle = taskTitle ?: existingTodo.taskTitle,
                    taskDescription = taskDescription ?: existingTodo.taskDescription,
                    startTime = startTime ?: existingTodo.startTime,
                    endTime = endTime ?: existingTodo.endTime,
                    date = date ?: existingTodo.date,
                    updatedAt = System.currentTimeMillis().toString(),
                    isLocalOnly = true
                )
                todoDao.updateTodo(updatedTodo)
            }

            Log.d("TodoRepository", "Offline - storing update for later sync")
            Result.failure(Exception("Offline - update will be synced later"))
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error updating todo", e)
            Result.failure(e)
        }
    }

    suspend fun deleteTodo(token: String, todoId: String): Result<String> {
        return try {
            val response = todoApi.deleteTodo("Bearer $token", todoId)

            if (response.isSuccessful && response.body()?.success == true) {
                todoDao.deleteTodoById(todoId)
                Result.success(response.body()?.message ?: "Task deleted!")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to delete task"))
            }
        } catch (e: IOException) {
            todoDao.deleteTodoById(todoId)

            Log.d("TodoRepository", "Offline - deleting locally for later sync")
            Result.failure(Exception("Offline - deletion will be synced later"))
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error deleting todo", e)
            Result.failure(e)
        }
    }

    suspend fun toggleTodoStatus(token: String, todoId: String): Result<String> {
        return try {
            val response = todoApi.toggleTodoStatus("Bearer $token", todoId)

            if (response.isSuccessful && response.body()?.success == true) {
                val updatedTodo = response.body()?.data
                if (updatedTodo != null) {
                    todoDao.insertTodo(TodoEntity.fromTodoItem(updatedTodo))
                }
                Result.success(response.body()?.message ?: "Task status updated!")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to toggle task status"))
            }
        } catch (e: IOException) {
            val existingTodo = todoDao.getTodoById(todoId)
            if (existingTodo != null) {
                todoDao.toggleTodoStatus(todoId, !existingTodo.isDone)
            }

            Log.d("TodoRepository", "Offline - toggling status for later sync")
            Result.failure(Exception("Offline - status change will be synced later"))
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error toggling todo status", e)
            Result.failure(e)
        }
    }
}