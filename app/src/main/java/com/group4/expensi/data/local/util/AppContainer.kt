package com.group4.expensi.data.local.util

import android.content.Context
import com.group4.expensi.data.local.ExpensiDatabase
import com.group4.expensi.data.local.repository.OfflineTransactionRepository
import com.group4.expensi.data.local.repository.TransactionRepository

interface AppContainer {
    val transactionRepository: TransactionRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val transactionRepository: TransactionRepository by lazy {
        OfflineTransactionRepository(ExpensiDatabase.getDatabase(context).transactionDao())
    }
}