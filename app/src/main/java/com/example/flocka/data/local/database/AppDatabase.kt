package com.example.flocka.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flocka.data.local.dao.CommunityDao
import com.example.flocka.data.local.dao.EventDao
import com.example.flocka.data.local.dao.OrderDao
import com.example.flocka.data.local.dao.PendingOperationDao
import com.example.flocka.data.local.dao.SpaceDao
import com.example.flocka.data.local.dao.QuizDao
import com.example.flocka.data.local.dao.TodoDao
import com.example.flocka.data.local.entity.CommunityEntity
import com.example.flocka.data.local.entity.EventEntity
import com.example.flocka.data.local.entity.OrderEntity
import com.example.flocka.data.local.entity.PendingOperationEntity
import com.example.flocka.data.local.entity.SpaceEntity
import com.example.flocka.data.local.entity.QuizEntity
import com.example.flocka.data.local.entity.QuizResultEntity
import com.example.flocka.data.local.entity.TodoEntity

@Database(
    entities = [
        CommunityEntity::class,
        QuizEntity::class,
        QuizResultEntity::class,
        SpaceEntity::class,
        OrderEntity::class,
        TodoEntity::class,
        EventEntity::class,
        PendingOperationEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao
    abstract fun todoDao(): TodoDao
    abstract fun spaceDao(): SpaceDao
    abstract fun quizDao(): QuizDao
    abstract fun orderDao(): OrderDao
    abstract fun eventDao() : EventDao
    abstract fun pendingOperationDao() : PendingOperationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flocka_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}