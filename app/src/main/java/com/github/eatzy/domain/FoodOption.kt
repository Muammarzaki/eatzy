package com.github.eatzy.domain

import android.content.Context
import com.github.eatzy.R

enum class FoodOption {
    STOCK,
    Wasted;

    fun getLabel(context: Context): String {
        return when (this) {
            STOCK -> context.getString(R.string.food_option_stock)
            Wasted -> context.getString(R.string.food_option_wasted)
        }
    }
}