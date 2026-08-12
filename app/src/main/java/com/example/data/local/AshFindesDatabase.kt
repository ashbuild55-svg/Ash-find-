package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChatSessionEntity::class,
        ChatMessageEntity::class,
        GeneratedImageEntity::class,
        UserSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AshFindesDatabase : RoomDatabase() {
    abstract fun dao(): AshFindesDao

    companion object {
        @Volatile
        private var INSTANCE: AshFindesDatabase? = null

        fun getInstance(context: Context): AshFindesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AshFindesDatabase::class.java,
                    "ash_findes_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
