package com.example.flocka.di

import android.content.Context
import com.example.flocka.data.local.database.AppDatabase
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.repository.CommunityRepository
import com.example.flocka.data.repository.EventRepository
import com.example.flocka.data.repository.OrderRepository
import com.example.flocka.data.repository.SpaceRepository
import com.example.flocka.data.repository.QuizRepository
import com.example.flocka.data.repository.TodoRepository

object AppModule {
    fun provideCommunityRepository(context: Context): CommunityRepository {
        val database = AppDatabase.getInstance(context)
        val communityApi = RetrofitClient.communityApi
        return CommunityRepository(communityApi, database.communityDao())
    }

    fun provideQuizRepository(context: Context): QuizRepository {
        val database = AppDatabase.getInstance(context)
        val quizApi = RetrofitClient.quizApi
        return QuizRepository(quizApi, database.quizDao())
    }

    fun provideSpaceRepository(context: Context): SpaceRepository {
        val database = AppDatabase.getInstance(context)
        val spaceApi = RetrofitClient.spaceApi
        return SpaceRepository(spaceApi, database.spaceDao())
    }

    fun provideOrderRepository(context: Context): OrderRepository {
        val database = AppDatabase.getInstance(context)
        val orderApi = RetrofitClient.orderApi
        return OrderRepository(orderApi, database.orderDao())
    }
    fun provideTodoRepository(context: Context): TodoRepository {
        val database = AppDatabase.getInstance(context)
        val todoApi = RetrofitClient.todoApi
        return TodoRepository(todoApi, database.todoDao())
    }
    fun provideEventRepository(context: Context): EventRepository {
        val database = AppDatabase.getInstance(context)
        val eventApi = RetrofitClient.eventApi
        return EventRepository(eventApi, database.eventDao(), database.pendingOperationDao(), context)
    }
}