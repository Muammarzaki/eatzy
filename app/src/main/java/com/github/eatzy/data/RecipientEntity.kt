package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.eatzy.domain.Recipient
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
    val description: String? = null,
    @ColumnInfo(name = "current_score")
    val currentScore: Int? = 0,
) {

    fun toDomain(): Recipient = Recipient(
        id = id,
        recipientName = recipientName,
        address = address,
        contact = contact,
        type = type,
        description = description ?: "",
    )

    companion object {
        fun from(recipient: Recipient): RecipientEntity = RecipientEntity(
            id = recipient.id,
            recipientName = recipient.recipientName,
            address = recipient.address,
            contact = recipient.contact,
            type = recipient.type,
            description = recipient.description,
        )
    }
}