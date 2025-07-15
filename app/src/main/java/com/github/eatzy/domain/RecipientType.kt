package com.github.eatzy.domain

enum class RecipientType(val label: String) {
    SOCIAL("Social"), LIVESTOCK_COMPOST("Livestock Compost");

    override fun toString(): String  = label
}