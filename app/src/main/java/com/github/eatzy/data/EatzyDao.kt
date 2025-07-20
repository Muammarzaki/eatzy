package com.github.eatzy.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.FoodWasteChartData
import com.github.eatzy.domain.LeftoverStatus
import com.github.eatzy.domain.WastedFoodTrend

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

    @Query("SELECT * FROM wasted_food where unit = :unit ORDER BY leftover_input_date DESC ")
    fun getAllWastedFoodByUnit(unit: FoodUnit): PagingSource<Int, WastedWithFoodItems>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWastedFood(wastedFood: WastedFoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFoodItem(foodItem: FoodItemEntity): Long

    @Update
    suspend fun updateFoodItem(foodItem: FoodItemEntity)

    @Update
    suspend fun updateWastedFood(wastedFood: WastedFoodEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDistribution(distribution: DistributionEntity): Long

    @Query("SELECT * FROM users WHERE owner_name = :username")
    suspend fun getUserByUsername(username: String): UserWithBusinesses?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBusiness(business: BusinessEntity): Long

    @Query("SELECT * FROM wasted_food WHERE id = :id")
    suspend fun getWastedFoodById(id: Int): WastedWithFoodItems?

    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getFoodItemById(id: Int): FoodItemEntity?


    @Query(
        """SELECT
      (SELECT SUM(wf.leftover_quantity)
       FROM distributions d
       JOIN wasted_food wf ON d.leftover_food_id = wf.id) AS distributed,
    
      (SELECT SUM(leftover_quantity)
       FROM wasted_food
       WHERE condition = 'DISPOSED') AS wasted,
    
      (SELECT SUM(leftover_quantity)
       FROM wasted_food
       WHERE status = 'AVAILABLE'
         AND condition != 'DISPOSED'
         AND id NOT IN (SELECT leftover_food_id FROM distributions)
      ) AS remaining"""
    )
    suspend fun getFoodWasteChartData(): FoodWasteChartData

    @Query(
        """
    SELECT
      strftime('%m', datetime(leftover_input_date / 1000, 'unixepoch')) AS month,
      SUM(leftover_quantity) AS wastedTotal
    FROM
      wasted_food
    WHERE
      strftime('%Y', datetime(leftover_input_date / 1000, 'unixepoch')) = :currentYear
    GROUP BY
      month
    ORDER BY
      month ASC
"""
    )
    suspend fun getWastedFoodEachMonth(currentYear: String): List<WastedFoodTrend>
}