package com.github.eatzy.domain

enum class FoodForm(override val label: String) : LabelEnum {
    SOLID("Solid"),
    LIQUID("Liquid");

    override fun toString() = label
}