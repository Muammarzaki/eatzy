package com.github.eatzy.data

import androidx.room.Embedded
import androidx.room.Relation
import com.github.eatzy.domain.WastedFood

data class WastedWithFoodItems(
    @Embedded val wastedFood: WastedFoodEntity,

    @Relation(
        parentColumn = "food_item_id",
        entityColumn = "id"
    )
    val foodItem: FoodItemEntity
)

data class UserWithBusinesses(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val businesses: BusinessEntity
)

data class DetailedDistribution(
    @Embedded val distribution: DistributionEntity,
    @Relation(
        parentColumn = "recipient_id",
        entityColumn = "id"
    )
    val recipient: RecipientEntity,
    @Relation(
        parentColumn = "leftover_food_id",
        entityColumn = "id",
        entity = WastedFoodEntity::class
    )
    val wastedFood: WastedWithFoodItems,
)

