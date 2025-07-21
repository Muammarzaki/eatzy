package com.github.eatzy.domain

import android.content.Context
import com.github.eatzy.R

enum class DistributionOption {
    SEND,
    SENT;

    fun getLabel(context: Context): String {
        return when (this) {
            SEND -> context.getString(R.string.distribution_option_send)
            SENT -> context.getString(R.string.distribution_option_sent)
        }
    }
}
