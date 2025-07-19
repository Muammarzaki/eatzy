package com.github.eatzy.domain

import android.content.Context
import com.github.eatzy.R

enum class DistributionOption {
    Send,
    Sent;

    fun getLabel(context: Context): String {
        return when (this) {
            Send -> context.getString(R.string.distribution_option_send)
            Sent -> context.getString(R.string.distribution_option_sent)
        }
    }
}
