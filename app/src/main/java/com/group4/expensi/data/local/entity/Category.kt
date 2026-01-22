package com.group4.expensi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val catId:Long,
    val catTitle:String,
    val catIconUrl: String
)
