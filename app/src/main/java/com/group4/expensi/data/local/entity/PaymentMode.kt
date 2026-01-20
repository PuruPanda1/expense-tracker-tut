package com.group4.expensi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_modes")
data class PaymentMode(
    @PrimaryKey(autoGenerate = true)
    val ptId:Long,
    val ptTitle: String,
    val ptDescription: String,
    val ptStartingBalance: Float,
    val ptIconUrl: String
)
