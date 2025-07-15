package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodUnit
import java.util.Date


@Entity(
    tableName = "food_items",
    foreignKeys = [
        ForeignKey(
            entity = BusinessEntity::class,
            parentColumns = ["id"],
            childColumns = ["business_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "business_id", index = true)
    val businessId: Int,
    @ColumnInfo(name = "food_name")
    val foodName: String,
    @ColumnInfo(name = "food_type")
    val foodType: String,
    @ColumnInfo(name = "expiration_date")
    val expirationDate: Date,
    @ColumnInfo(name = "initial_quantity")
    val initialQuantity: Double,
    val unit: FoodUnit,
    @ColumnInfo(name = "input_date")
    val inputDate: Date = Date()
) {
    fun toDomain(): FoodItem =
        FoodItem(
            id = this.id,
            businessId = this.businessId,
            foodName = this.foodName,
            foodType = this.foodType,
            expirationDate = this.expirationDate,
            initialQuantity = this.initialQuantity,
            unit = this.unit,
            inputDate = this.inputDate
        )
}