package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.eatzy.domain.RecipientType


@Entity(tableName = "recipients")
data class RecipientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "recipient_name")
    val recipientName: String,
    val address: String,
    val contact: String,
    val type: RecipientType,
    @ColumnInfo(name = "current_score")
    val currentScore: Int? = 0,
)