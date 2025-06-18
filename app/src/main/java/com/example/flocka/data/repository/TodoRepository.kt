package com.example.flocka.data.repository

import android.util.Log
import com.example.flocka.data.api.TodoApi
import com.example.flocka.data.local.dao.TodoDao
import com.example.flocka.data.local.entity.TodoEntity
import com.example.flocka.data.model.CreateTodoRequest
import com.example.flocka.data.model.TodoItem
import com.example.flocka.data.model.UpdateTodoRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class TodoRepository(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {

    suspend fun getTodos(token: String): Result<List<TodoItem>> {
        return try {
            syncPendingChanges(token)

            val response = todoApi.getTodos("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val serverTodos = response.body()?.data ?: emptyList()

                todoDao.clearSyncedTodos()
                val todoEntities = serverTodos.map { TodoEntity.fromTodoItem(it) }
                todoDao.insertTodos(todoEntities)

                val allTodos = todoDao.getAllTodos().map { it.toTodoItem() }
                Log.d("TodoRepository", "Fetched ${serverTodos.size} todos from server, ${allTodos.size} total with local changes")

                Result.success(allTodos)
            } else {
                val cachedTodos = todoDao.getAllTodos().map { it.toTodoItem() }
                Log.d("TodoRepository", "Server error - using ${cachedTodos.size} cached todos")
                Result.success(cachedTodos)
            }
        } catch (e: IOException) {
            val cachedTodos = todoDao.getAllTodos().map { it.toTodoItem() }
            Log.d("TodoRepository", "Network error - using ${cachedTodos.size} cached todos")
            Result.success(cachedTodos)
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error fetching todos", e)
            val cachedTodos = todoDao.getAllTodos().map { it.toTodoItem() }
            if (cachedTodos.isNotEmpty()) {
                Result.success(cachedTodos)
            } else {
                Result.failure(e)
            }
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
        val localId = UUID.randomUUID().toString()
        val currentTime = System.currentTimeMillis().toString()

        val localTodo = TodoEntity(
            todoId = localId,
            userId = "",
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            startTime = startTime,
            endTime = endTime,
            date = date,
            isDone = false,
            createdAt = currentTime,
            updatedAt = currentTime,
            isLocalOnly = true,
            isSynced = false,
            syncOperation = "CREATE"
        )

        todoDao.insertTodo(localTodo)
        Log.d("TodoRepository", "Todo created locally: $localId")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = CreateTodoRequest(taskTitle, taskDescription, startTime, endTime, date)
                val response = todoApi.createTodo("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val serverTodo = response.body()?.data
                    if (serverTodo != null) {
                        todoDao.deleteTodoById(localId)
                        todoDao.insertTodo(TodoEntity.fromTodoItem(serverTodo))
                        Log.d("TodoRepository", "Todo synced successfully: ${serverTodo.todoId}")
                    }
                } else {
                    Log.w("TodoRepository", "Failed to sync todo creation: ${response.body()?.message}")
                }
            } catch (e: Exception) {
                Log.w("TodoRepository", "Background sync failed for todo creation", e)
            }
        }

        return Result.success("Task added!")
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
        val existingTodo = todoDao.getTodoById(todoId)
        if (existingTodo != null) {
            val updatedTodo = existingTodo.copy(
                taskTitle = taskTitle ?: existingTodo.taskTitle,
                taskDescription = taskDescription ?: existingTodo.taskDescription,
                startTime = startTime ?: existingTodo.startTime,
                endTime = endTime ?: existingTodo.endTime,
                date = date ?: existingTodo.date,
                updatedAt = System.currentTimeMillis().toString(),
                isSynced = false,
                syncOperation = "UPDATE"
            )
            todoDao.updateTodo(updatedTodo)
            Log.d("TodoRepository", "Todo updated locally: $todoId")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val request = UpdateTodoRequest(taskTitle, taskDescription, startTime, endTime, date)
                    val response = todoApi.updateTodo("Bearer $token", todoId, request)

                    if (response.isSuccessful && response.body()?.success == true) {
                        val serverTodo = response.body()?.data
                        if (serverTodo != null) {
                            todoDao.insertTodo(TodoEntity.fromTodoItem(serverTodo))
                            Log.d("TodoRepository", "Todo update synced successfully: $todoId")
                        }
                    } else {
                        Log.w("TodoRepository", "Failed to sync todo update: ${response.body()?.message}")
                    }
                } catch (e: Exception) {
                    Log.w("TodoRepository", "Background sync failed for todo update", e)
                }
            }

            return Result.success("Task updated!")
        } else {
            return Result.failure(Exception("Todo not found locally"))
        }
    }

    suspend fun deleteTodo(token: String, todoId: String): Result<String> {
        todoDao.softDeleteTodo(todoId)
        Log.d("TodoRepository", "Todo soft deleted locally: $todoId")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = todoApi.deleteTodo("Bearer $token", todoId)

                if (response.isSuccessful && response.body()?.success == true) {
                    todoDao.markTodoAsSynced(todoId)
                    todoDao.hardDeleteSyncedTodo(todoId)
                    Log.d("TodoRepository", "Todo deletion synced successfully: $todoId")
                } else {
                    Log.w("TodoRepository", "Failed to sync todo deletion: ${response.body()?.message}")
                }
            } catch (e: Exception) {
                Log.w("TodoRepository", "Background sync failed for todo deletion", e)
            }
        }

        return Result.success("Task deleted!")
    }

    suspend fun toggleTodoStatus(token: String, todoId: String): Result<String> {
        val existingTodo = todoDao.getTodoById(todoId)
        if (existingTodo != null) {
            val newStatus = !existingTodo.isDone

            todoDao.toggleTodoStatus(todoId, newStatus, isSynced = false, syncOperation = "UPDATE")
            Log.d("TodoRepository", "Todo status toggled locally: $todoId -> $newStatus")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = todoApi.toggleTodoStatus("Bearer $token", todoId)

                    if (response.isSuccessful && response.body()?.success == true) {
                        val serverTodo = response.body()?.data
                        if (serverTodo != null) {
                            todoDao.insertTodo(TodoEntity.fromTodoItem(serverTodo))
                            Log.d("TodoRepository", "Todo status sync successful: $todoId")
                        }
                    } else {
                        Log.w("TodoRepository", "Failed to sync todo status: ${response.body()?.message}")
                    }
                } catch (e: Exception) {
                    Log.w("TodoRepository", "Background sync failed for todo status", e)
                }
            }

            return Result.success("Task status updated!")
        } else {
            return Result.failure(Exception("Todo not found locally"))
        }
    }

    private suspend fun syncPendingChanges(token: String) {
        try {
            val todosToCreate = todoDao.getTodosToCreate()
            for (todo in todosToCreate) {
                try {
                    val request = CreateTodoRequest(
                        todo.taskTitle, todo.taskDescription,
                        todo.startTime, todo.endTime, todo.date
                    )
                    val response = todoApi.createTodo("Bearer $token", request)

                    if (response.isSuccessful && response.body()?.success == true) {
                        val serverTodo = response.body()?.data
                        if (serverTodo != null) {
                            todoDao.deleteTodoById(todo.todoId)
                            todoDao.insertTodo(TodoEntity.fromTodoItem(serverTodo))
                        }
                    }
                } catch (e: Exception) {
                    Log.w("TodoRepository", "Failed to sync create for ${todo.todoId}", e)
                }
            }

            val todosToUpdate = todoDao.getTodosToUpdate()
            for (todo in todosToUpdate) {
                if (todo.syncOperation == "UPDATE") {
                    try {
                        val request = UpdateTodoRequest(
                            todo.taskTitle, todo.taskDescription,
                            todo.startTime, todo.endTime, todo.date
                        )
                        val response = todoApi.updateTodo("Bearer $token", todo.todoId, request)

                        if (response.isSuccessful && response.body()?.success == true) {
                            val serverTodo = response.body()?.data
                            if (serverTodo != null) {
                                todoDao.insertTodo(TodoEntity.fromTodoItem(serverTodo))
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("TodoRepository", "Failed to sync update for ${todo.todoId}", e)
                    }
                }
            }

            val todosToDelete = todoDao.getTodosToDelete()
            for (todo in todosToDelete) {
                try {
                    val response = todoApi.deleteTodo("Bearer $token", todo.todoId)

                    if (response.isSuccessful && response.body()?.success == true) {
                        todoDao.hardDeleteSyncedTodo(todo.todoId)
                    }
                } catch (e: Exception) {
                    Log.w("TodoRepository", "Failed to sync delete for ${todo.todoId}", e)
                }
            }

            Log.d("TodoRepository", "Sync completed: ${todosToCreate.size} creates, ${todosToUpdate.size} updates, ${todosToDelete.size} deletes")

        } catch (e: Exception) {
            Log.e("TodoRepository", "Error during sync", e)
        }
    }

    suspend fun getLocalTodos(): List<TodoItem> {
        return try {
            todoDao.getAllTodos().map { it.toTodoItem() }
        } catch (e: Exception) {
            Log.e("TodoRepository", "Error getting local todos", e)
            emptyList()
        }
    }
}