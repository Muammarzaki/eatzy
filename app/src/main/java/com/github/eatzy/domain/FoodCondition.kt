package com.github.eatzy.domain

import android.content.Context
import com.github.eatzy.R


enum class FoodCondition {
    EDIBLE,
    ANIMAL_FEED_COMPOST,
    DISPOSED;

    fun getLabel(context: Context): String {
        return when (this) {
            EDIBLE -> context.getString(R.string.food_condition_edible)
            ANIMAL_FEED_COMPOST -> context.getString(R.string.food_condition_animal_feed_compost)
            DISPOSED -> context.getString(R.string.food_condition_disposed)
        }
    }

}