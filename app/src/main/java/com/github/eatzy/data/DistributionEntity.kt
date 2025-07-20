package com.github.eatzy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.github.eatzy.domain.DistributionStatus
import java.util.Date

@Entity(
    tableName = "distributions",
    foreignKeys = [
        ForeignKey(
            entity = WastedFoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["leftover_food_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipientEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipient_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DistributionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "leftover_food_id", index = true)
    val leftoverFoodId: Int,
    @ColumnInfo(name = "recipient_id", index = true)
    val recipientId: Int,
    @ColumnInfo(name = "distribution_date")
    val distributionDate: Date,
    val notes: String?,
    val status: DistributionStatus = DistributionStatus.PACKING
)