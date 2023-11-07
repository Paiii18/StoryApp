package com.example.submission1storyapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submission1storyapp.data.database.dao.RemotekeyDao
import com.example.submission1storyapp.data.database.dao.StoryDao

@Database(entities = [Remotekeys::class, Entities::class], version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemotekeyDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            if (INSTANCE == null) {
                synchronized(StoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StoryDatabase::class.java, "story_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE as StoryDatabase
        }
    }
}