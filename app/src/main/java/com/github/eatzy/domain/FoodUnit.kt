package com.github.eatzy.domain

import android.content.Context
import com.github.eatzy.R

enum class FoodUnit {
    KILOGRAM,
    LITER,
    PACK,
    PORTION,
    PIECE;

    fun getLabel(context: Context): String {
        return when (this) {
            KILOGRAM -> context.getString(R.string.unit_kilogram)
            LITER -> context.getString(R.string.unit_liter)
            PACK -> context.getString(R.string.unit_pack)
            PORTION -> context.getString(R.string.unit_portion)
            PIECE -> context.getString(R.string.unit_piece)
        }
    }
}
