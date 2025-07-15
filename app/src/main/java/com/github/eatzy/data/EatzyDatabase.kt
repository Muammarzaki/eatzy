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
        WastedFoodEntity::class,
        UnreadableNotificationEntity::class,
    ],
    version = 4
)
@TypeConverters(DatabaseTypeConverter::class)
abstract class EatzyDatabase : RoomDatabase() {
    abstract fun eatzyDao(): EatzyDao

    companion object {
        @Volatile
        private var INSTANCE: EatzyDatabase? = null

        fun getInstance(context: Context): EatzyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EatzyDatabase::class.java,
                    "eatzy_database"
                ).fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}