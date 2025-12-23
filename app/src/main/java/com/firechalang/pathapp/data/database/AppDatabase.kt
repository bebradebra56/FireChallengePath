package com.firechalang.pathapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.firechalang.pathapp.data.dao.ChallengeDao
import com.firechalang.pathapp.data.dao.DailyCheckInDao
import com.firechalang.pathapp.data.model.Challenge
import com.firechalang.pathapp.data.model.DailyCheckIn

@Database(
    entities = [Challenge::class, DailyCheckIn::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun dailyCheckInDao(): DailyCheckInDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fire_challenge_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

