package com.github.eatzy.domain

enum class DistributionStatus(val label: String) {
    PACKING("Packing"),
    EN_ROUTE("En Route"),
    RECEIVED("Received");

    override fun toString(): String = label
}

