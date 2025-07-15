package com.github.eatzy.domain

import java.util.Date

data class FoodItem(
    val id: Int? = null,
    val businessId: Int? = null,
    val foodName: String,
    val foodType: String,
    val expirationDate: Date,
    val initialQuantity: Double,
    val unit: FoodUnit,
    val inputDate: Date? = null
)
