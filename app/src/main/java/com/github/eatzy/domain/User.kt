package com.github.eatzy.domain

data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String? = null,
    val business: Business
)