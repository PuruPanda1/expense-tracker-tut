package com.group4.expensi.ui

import com.group4.expensi.data.local.entity.Transaction

data class TransactionUiState(
    val transactions : List<Transaction> = emptyList()
)