package com.group4.expensi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val tId:Long=0L,
    val tTitle: String,
    val tDescription: String,
    val tAmount: Float,
    val tDate: Date,
    val tIsExpense: Boolean,
    val tPaymentModeId: Long,
    val tCategoryId: Long
)
