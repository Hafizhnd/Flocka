package com.example.flocka.data.model

import com.google.gson.annotations.SerializedName

data class TodoItem(
    @SerializedName("todo_id") val todoId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("task_title") val taskTitle: String,
    @SerializedName("task_description") val taskDescription: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("is_done") private val isDoneInt: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
) {
    val isDone: Boolean
        get() = isDoneInt == 1
}
data class CreateTodoRequest(
    @SerializedName("task_title") val taskTitle: String,
    @SerializedName("task_description") val taskDescription: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("date") val date: String?
)

data class UpdateTodoRequest(
    @SerializedName("task_title") val taskTitle: String?,
    @SerializedName("task_description") val taskDescription: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("date") val date: String?
)

data class TodoSuccessResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)