package com.example.flocka.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flocka.data.local.dao.CommunityDao
import com.example.flocka.data.local.entity.CommunityEntity

@Database(
    entities = [CommunityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flocka_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 