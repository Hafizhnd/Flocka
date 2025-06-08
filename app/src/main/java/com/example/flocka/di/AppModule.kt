package com.example.flocka.di

import android.content.Context
import com.example.flocka.data.local.database.AppDatabase
import com.example.flocka.data.remote.CommunityApi
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.data.repository.CommunityRepository

object AppModule {
    fun provideCommunityRepository(context: Context): CommunityRepository {
        val database = AppDatabase.getInstance(context)
        val communityApi = RetrofitClient.communityApi
        return CommunityRepository(communityApi, database.communityDao())
    }
} 