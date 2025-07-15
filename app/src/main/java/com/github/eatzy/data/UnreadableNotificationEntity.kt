package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.eatzy.domain.UnreadableNotification
import java.util.Date

@Entity(tableName = "unreadable_notifications")
data class UnreadableNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "subject")
    val subject: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Date = Date(),
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false,
) {
    fun toDomain(): UnreadableNotification = UnreadableNotification(
        id = id,
        subject = subject,
        message = message,
        timestamp = timestamp
    )
}