package com.github.eatzy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Unit::class], version = 1)
abstract class EatzyDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: EatzyDatabase? = null

        fun getDatabase(context: Context): EatzyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EatzyDatabase::class.java,
                    "eatzy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}