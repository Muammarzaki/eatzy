package com.github.eatzy.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.eatzy.domain.LeftoverStatus

@Dao
interface EatzyDao {
    @Transaction
    @Query("SELECT * FROM wasted_food ORDER BY id DESC")
    fun getAllWastedFood(): PagingSource<Int, WastedWithFoodItems>

    @Query("SELECT * FROM food_items ORDER BY id DESC")
    fun getAllFood(): PagingSource<Int, FoodItemEntity>

    @Query("SELECT * FROM recipients ORDER BY id DESC")
    fun getAllRecipes(): PagingSource<Int, RecipientEntity>

    @Query("SELECT * FROM wasted_food WHERE status = :status")
    fun getAllWastedFoodByStatus(status: LeftoverStatus): PagingSource<Int, WastedWithFoodItems>

    @Query("SELECT * FROM distributions ORDER BY id DESC")
    fun getAllDistributions(): PagingSource<Int, DetailedDistribution>

    @Query("SELECT * FROM unreadable_notifications where is_read = 0 ORDER BY timestamp DESC ")
    fun getAllUnreadableNotifications(): PagingSource<Int, UnreadableNotificationEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWastedFood(wastedFood: WastedFoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFoodItem(foodItem: FoodItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDistribution(distribution: DistributionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecipient(recipient: RecipientEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUnreadableNotification(unreadableNotification: UnreadableNotificationEntity): Long

    @Query("UPDATE unreadable_notifications SET is_read = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Int)

    @Query("SELECT * FROM users WHERE owner_name = :username")
    suspend fun getUserByUsername(username: String): UserWithBusinesses?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBusiness(business: BusinessEntity): Long

    @Query("SELECT * FROM wasted_food WHERE id = :id")
    suspend fun getWastedFoodById(id: Int): WastedWithFoodItems?
}