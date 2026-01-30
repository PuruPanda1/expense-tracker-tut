package com.group4.expensi.ui.transaction

import com.group4.expensi.data.local.entity.Transaction

data class TransactionUiState(
    val transactions : List<Transaction> = emptyList(),
    val selectedCategoryId: Long? = null,
    val selectedPaymentModeId: Long? = null,
    val sortType: SortType = SortType.DATE_DESC,
    val pageSize: Int = 10,
    val currentLimit: Int = 10,
    val isEndReached: Boolean = false
)
enum class SortType {
    DATE_DESC,
    DATE_ASC,
    AMOUNT_DESC,
    AMOUNT_ASC
}