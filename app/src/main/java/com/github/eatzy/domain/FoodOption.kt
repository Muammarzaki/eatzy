package com.github.eatzy.domain

enum class FoodOption(override val label: String) : LabelEnum {
    Stock("Stock"),
    Wasted("Wasted");

    override fun toString(): String = label
}