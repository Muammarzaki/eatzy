package com.github.eatzy.domain

import android.content.Context
import com.github.eatzy.R

enum class FoodForm {
    SOLID,
    LIQUID;

    fun getLabel(context: Context): String {
        return when (this) {
            SOLID -> context.getString(R.string.food_form_solid)
            LIQUID -> context.getString(R.string.food_form_liquid)
        }
    }

}