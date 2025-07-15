package com.github.eatzy.domain

import java.util.Date

data class UnreadableNotification(
    val id: Int,
    val subject: String,
    val message: String,
    val timestamp: Date,
)
