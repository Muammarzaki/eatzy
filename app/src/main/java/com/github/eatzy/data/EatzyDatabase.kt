package com.github.eatzy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        UserEntity::class,
        BusinessEntity::class,
        FoodItemEntity::class,
        RecipientEntity::class,
        DistributionEntity::class,
        LeftoverFoodEntity::class
    ],
    version = 1
)
@TypeConverters(DatabaseTypeConverter::class)
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