package com.github.eatzy.domain


enum class FoodCondition(override val label: String) : LabelEnum {
    EDIBLE("Edible"),
    ANIMAL_FEED_COMPOST("Animal Feed/Compost"),
    DISPOSED("Disposed");

    override fun toString(): String = label
}