package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.github.eatzy.domain.FoodCondition
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.LeftoverStatus
import java.util.Date


@Entity(
    tableName = "leftover_food",
    foreignKeys = [
        ForeignKey(
            entity = FoodItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["food_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LeftoverFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "food_item_id", index = true)
    val foodItemId: Int,
    @ColumnInfo(name = "leftover_input_date")
    val leftoverInputDate: Date = Date(),
    @ColumnInfo(name = "leftover_quantity")
    val leftoverQuantity: Float,
    val unit: FoodUnit,
    @ColumnInfo(name = "expiration_date")
    val expirationDate: Date,
    val condition: FoodCondition,
    val form: FoodForm,
    val status: LeftoverStatus = LeftoverStatus.AVAILABLE
)