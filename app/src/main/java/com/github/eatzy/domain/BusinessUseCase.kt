package com.github.eatzy.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface BusinessUseCase {
    fun getAllWasted(): Flow<PagingData<WastedFood>>
    fun getAllFood(): Flow<PagingData<FoodItem>>
    fun getAllRecipient(): Flow<PagingData<Recipient>>
    fun getAllUndistributedWastedFood(): Flow<PagingData<WastedFood>>
    fun getAllDistributedWastedFood(): Flow<PagingData<Distributed>>
    fun getUnreadableNotification(): Flow<PagingData<UnreadableNotification>>

    suspend fun saveFoodStock(foodItem: FoodItem, businessId: Int)
    suspend fun saveWastedFood(wastedFood: WastedFood)
    suspend fun saveUser(user: User): Int
    suspend fun saveBusiness(business: Business)
    suspend fun findUserByName(username: String): User?
    suspend fun findWastedFoodById(id: Int): WastedFood?
}