package com.group4.expensi.viewModel.transaction

import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import java.util.Date

data class EntryTransactionUiState(
    val amount: String = "",
    val title: String = "",
    val description: String = "",
    val isExpense: Boolean = true,
    val selectedCategory: Category? = null,
    val selectedPaymentMode: PaymentMode? = null,
    val selectedDate: Date = Date(),
    val isSaving: Boolean = false
)