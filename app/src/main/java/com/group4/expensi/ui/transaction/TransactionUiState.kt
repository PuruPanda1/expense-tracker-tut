package com.group4.expensi.ui.transaction

import com.group4.expensi.data.local.entity.Transaction

data class TransactionUiState(
    val transactions : List<Transaction> = emptyList()
)