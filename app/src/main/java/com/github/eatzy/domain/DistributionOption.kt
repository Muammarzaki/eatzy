package com.github.eatzy.domain

enum class DistributionOption(val label: String) {
    Send("Send"),
    Sent("Sent");

    override fun toString(): String = label
}
