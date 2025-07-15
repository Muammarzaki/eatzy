package com.github.eatzy.domain

enum class FoodUnit(override val label: String) : LabelEnum {
    KILOGRAM("Kg"),
    LITER("L"),
    PACK("Pack"),
    PORTION("Portion"),
    PIECE("Pcs");

    override fun toString() = label
}
