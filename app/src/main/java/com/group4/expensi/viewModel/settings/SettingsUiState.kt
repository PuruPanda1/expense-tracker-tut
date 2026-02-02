package com.group4.expensi.viewModel.settings

import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode

data class SettingsUiState(
    val categories: List<Category> = emptyList(),
    val paymentModes: List<PaymentMode> = emptyList(),

    val showAddCategoryDialog: Boolean = false,
    val showAddPaymentModeDialog: Boolean = false,

    val categoryToDelete: Category? = null,
    val paymentModeToDelete: PaymentMode? = null
)
