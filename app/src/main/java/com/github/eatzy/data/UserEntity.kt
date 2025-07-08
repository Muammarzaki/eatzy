package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "owner_name")
    val ownerName: String,
    val email: String,
    val password: String
)