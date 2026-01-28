package com.group4.expensi.ui.pages

import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.model.CategoryExpense

data class HomeUiState(
    val isLoading: Boolean = true,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0,
    val transactions: List<Transaction> = emptyList(),
    val categoryExpense: List<CategoryExpense> = emptyList(),
    val error: String? = null
)
