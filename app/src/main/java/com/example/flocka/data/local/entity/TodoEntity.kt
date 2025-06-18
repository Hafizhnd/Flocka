package com.example.flocka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flocka.data.model.TodoItem

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey
    val todoId: String,
    val userId: String,
    val taskTitle: String,
    val taskDescription: String?,
    val startTime: String?,
    val endTime: String?,
    val date: String?,
    val isDone: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val fetchedAt: Long = System.currentTimeMillis(),
    val isLocalOnly: Boolean = false,
    val isSynced: Boolean = true,
    val syncOperation: String? = null, // "CREATE", "UPDATE", "DELETE"
    val isDeleted: Boolean = false
) {
    fun toTodoItem(): TodoItem {
        return TodoItem(
            todoId = todoId,
            userId = userId,
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            startTime = startTime,
            endTime = endTime,
            date = date,
            isDoneInt = if (isDone) 1 else 0,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromTodoItem(todoItem: TodoItem): TodoEntity {
            return TodoEntity(
                todoId = todoItem.todoId,
                userId = todoItem.userId,
                taskTitle = todoItem.taskTitle,
                taskDescription = todoItem.taskDescription,
                startTime = todoItem.startTime,
                endTime = todoItem.endTime,
                date = todoItem.date,
                isDone = todoItem.isDone,
                createdAt = todoItem.createdAt,
                updatedAt = todoItem.updatedAt,
                isSynced = true
            )
        }
    }
}