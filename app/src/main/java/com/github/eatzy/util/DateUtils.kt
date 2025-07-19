package com.github.eatzy.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toStringDate(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(this)
}
