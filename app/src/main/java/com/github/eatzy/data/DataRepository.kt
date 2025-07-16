package com.github.eatzy.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.eatzy.domain.Business
import com.github.eatzy.domain.BusinessUseCase
import com.github.eatzy.domain.Distributed
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.LeftoverStatus
import com.github.eatzy.domain.Recipient
import com.github.eatzy.domain.UnreadableNotification
import com.github.eatzy.domain.User
import com.github.eatzy.domain.WastedFood
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class DataRepository(private val eatzyDao: EatzyDao) : BusinessUseCase {
    override fun getAllWasted(): Flow<PagingData<WastedFood>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { eatzyDao.getAllWastedFood() }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    WastedFood(
                        id = entity.wastedFood.id,
                        foodItemId = entity.wastedFood.foodItemId,
                        foodItem = entity.foodItem.foodName,
                        leftoverInputDate = entity.wastedFood.leftoverInputDate,
                        leftoverQuantity = entity.wastedFood.leftoverQuantity,
                        unit = entity.wastedFood.unit,
                        expirationDate = entity.wastedFood.expirationDate,
                        condition = entity.wastedFood.condition,
                        form = entity.wastedFood.form,
                        status = entity.wastedFood.status
                    )
                }
            }

    }

    override fun getAllFood(): Flow<PagingData<FoodItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { eatzyDao.getAllFood() }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override fun getAllRecipient(): Flow<PagingData<Recipient>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { eatzyDao.getAllRecipes() }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override fun getAllUndistributedWastedFood(): Flow<PagingData<WastedFood>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { eatzyDao.getAllWastedFoodByStatus(LeftoverStatus.AVAILABLE) }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    WastedFood(
                        id = entity.wastedFood.id,
                        foodItemId = entity.wastedFood.foodItemId,
                        foodItem = entity.foodItem.foodName,
                        leftoverInputDate = entity.wastedFood.leftoverInputDate,
                        leftoverQuantity = entity.wastedFood.leftoverQuantity,
                        unit = entity.wastedFood.unit,
                        expirationDate = entity.wastedFood.expirationDate,
                        condition = entity.wastedFood.condition,
                        form = entity.wastedFood.form,
                        status = entity.wastedFood.status,
                        difference = (entity.wastedFood.leftoverQuantity / entity.foodItem.initialQuantity)
                    )
                }
            }
    }

    override fun getAllDistributedWastedFood(): Flow<PagingData<Distributed>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { eatzyDao.getAllDistributions() }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    Distributed(
                        id = entity.distribution.id,
                        recipient = entity.recipient.toDomain(),
                        wastedFood = WastedFood(
                            id = entity.wastedFood.wastedFood.id,
                            foodItemId = entity.wastedFood.wastedFood.foodItemId,
                            foodItem = entity.wastedFood.foodItem.foodName,
                            leftoverInputDate = entity.wastedFood.wastedFood.leftoverInputDate,
                            leftoverQuantity = entity.wastedFood.wastedFood.leftoverQuantity,
                            unit = entity.wastedFood.wastedFood.unit,
                            expirationDate = entity.wastedFood.wastedFood.expirationDate,
                            condition = entity.wastedFood.wastedFood.condition,
                            form = entity.wastedFood.wastedFood.form,
                            status = entity.wastedFood.wastedFood.status,
                            difference = (entity.wastedFood.wastedFood.leftoverQuantity / entity.wastedFood.foodItem.initialQuantity)
                        ),
                        distributionDate = entity.distribution.distributionDate,
                        status = entity.distribution.status,
                        notes = entity.distribution.notes,
                    )
                }
            }
    }

    override fun getUnreadableNotification(): Flow<PagingData<UnreadableNotification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { eatzyDao.getAllUnreadableNotifications() }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override suspend fun saveFoodStock(foodItem: FoodItem, businessId: Int) {
        eatzyDao.addFoodItem(
            FoodItemEntity(
                id = foodItem.id ?: 0,
                foodName = foodItem.foodName,
                initialQuantity = foodItem.initialQuantity,
                unit = foodItem.unit,
                expirationDate = foodItem.expirationDate,
                businessId = businessId,
                inputDate = Date(),
                foodType = foodItem.foodType
            )
        )
    }

    override suspend fun saveWastedFood(wastedFood: WastedFood) {
        eatzyDao.addWastedFood(
            WastedFoodEntity(
                id = wastedFood.id ?: 0,
                foodItemId = wastedFood.foodItemId,
                leftoverInputDate = wastedFood.leftoverInputDate ?: Date(),
                leftoverQuantity = wastedFood.leftoverQuantity,
                unit = wastedFood.unit,
                expirationDate = wastedFood.expirationDate,
                condition = wastedFood.condition,
                form = wastedFood.form,
                status = LeftoverStatus.AVAILABLE
            )
        )
    }

    override suspend fun saveUser(user: User): Int {
        return eatzyDao.addUser(
            UserEntity(
                id = user.id ?: 0,
                ownerName = user.name,
                email = user.email,
                phoneNumber = user.phoneNumber
            )
        ).toInt()
    }

    override suspend fun saveBusiness(business: Business) {
        eatzyDao.addBusiness(
            BusinessEntity(
                id = business.id ?: 0,
                businessName = business.businessName,
                address = business.address,
                userId = business.userId ?: 0
            )
        )
    }

    override suspend fun findUserByName(username: String): User? {
        return eatzyDao.getUserByUsername(username)?.let {
            User(
                id = it.user.id,
                name = it.user.ownerName,
                email = it.user.email,
                phoneNumber = it.user.phoneNumber,
                business = it.businesses.let { business ->
                    Business(
                        id = business.id,
                        businessName = business.businessName,
                        address = business.address,
                        userId = business.userId
                    )
                }
            )
        }
    }

    override suspend fun findWastedFoodById(id: Int): WastedFood? {
        return eatzyDao.getWastedFoodById(id)?.let {
            WastedFood(
                id = it.wastedFood.id,
                foodItemId = it.wastedFood.foodItemId,
                foodItem = it.foodItem.foodName,
                leftoverInputDate = it.wastedFood.leftoverInputDate,
                leftoverQuantity = it.wastedFood.leftoverQuantity,
                unit = it.wastedFood.unit,
                expirationDate = it.wastedFood.expirationDate,
                condition = it.wastedFood.condition,
                form = it.wastedFood.form,
                status = it.wastedFood.status,
                difference = (it.wastedFood.leftoverQuantity / it.foodItem.initialQuantity)
            )
        }
    }

    override suspend fun findFoodItemById(id: Int): FoodItem? {
        return eatzyDao.getFoodItemById(id)?.let {
            FoodItem(
                id = it.id,
                foodName = it.foodName,
                initialQuantity = it.initialQuantity,
                unit = it.unit,
                expirationDate = it.expirationDate,
                businessId = it.businessId,
                inputDate = it.inputDate,
                foodType = it.foodType
            )
        }
    }
}
