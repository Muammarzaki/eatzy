package com.github.eatzy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.eatzy.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    version = 6
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
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                val recipients = loadRecipientsFromRaw(context)
                                database.eatzyDao().addRecipients(recipients)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun loadRecipientsFromRaw(context: Context): List<RecipientEntity> {
            val inputStream = context.resources.openRawResource(R.raw.recipients)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val type = object : TypeToken<List<RecipientEntity>>() {}.type
            return gson.fromJson(jsonString, type)
        }
    }
}