package com.github.eatzy.domain

data class Recipient(
    val id: Int,
    val recipientName: String,
    val address: String,
    val contact: String,
    val description: String,
    val type: RecipientType
)