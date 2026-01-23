package com.group4.expensi.data.local.repository

import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactionsStream(): Flow<List<Transaction>>

    fun getTransactionStream(tId: Long): Flow<Transaction?>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)


    fun getAllCategoryStream(): Flow<List<Category>>

    fun getCategoryStream(tId: Long): Flow<Category?>

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)


    fun getAllPaymentModesStream(): Flow<List<PaymentMode>>

    fun getPaymentModeStream(tId: Long): Flow<PaymentMode?>

    suspend fun insertPaymentMode(paymentMode: PaymentMode)

    suspend fun updatePaymentMode(paymentMode: PaymentMode)

    suspend fun deletePaymentMode(paymentMode: PaymentMode)
}