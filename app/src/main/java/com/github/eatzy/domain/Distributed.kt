package com.github.eatzy.domain

import java.util.Date

data class Distributed(
    val id: Int,
    val recipient: Recipient,
    val wastedFood: WastedFood,
    val distributionDate: Date? = null,
    val notes: String? = null,
    val status: DistributionStatus? = null,
)