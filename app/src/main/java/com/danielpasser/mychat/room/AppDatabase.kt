package com.danielpasser.mychat.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danielpasser.mychat.models.room.UserEntity

@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: getDatabase(context).also { instance = it }
            }
        }

        private fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "User_DB"
                )
                    .fallbackToDestructiveMigration(false).build().also {
                        instance = it
                    }
            }
        }
    }
}