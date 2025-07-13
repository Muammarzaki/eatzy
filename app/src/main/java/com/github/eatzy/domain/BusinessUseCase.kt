package com.github.eatzy.domain

interface BusinessUseCase {
    fun getAllWasted()
    fun getAllFood()
    fun getAllRecipient()
    fun getAllDistribution()
    fun getUnreadableNotification()
}