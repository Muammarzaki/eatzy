package com.github.eatzy.domain

import java.util.Date

data class WastedFood(
    val id: Int? = null,
    val foodItemId: Int,
    val foodItem: String,
    val leftoverInputDate: Date? = null,
    val leftoverQuantity: Double,
    val unit: FoodUnit,
    val expirationDate: Date,
    val condition: FoodCondition,
    val form: FoodForm,
    val status: LeftoverStatus? = null,
    val difference: Double? = null,
)