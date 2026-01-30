package com.group4.expensi.data.local.repository

import com.group4.expensi.data.local.dao.TransactionDao
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.model.CategoryExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class OfflineTransactionRepository(private val transactionDao: TransactionDao) : TransactionRepository {
    override fun getAllTransactionsStream(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    override fun getTransactionStream(tId: Long): Flow<Transaction?> {
        return transactionDao.getTransaction(tId)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun getTransactionById(tId: Long): Transaction {
        return transactionDao.getTransaction(tId).first()
    }

    override fun getAllCategoryStream(): Flow<List<Category>> {
       return transactionDao.getAllCategories()
    }

    override fun getCategoryStream(catId: Long): Flow<Category?> {
       return transactionDao.getCategory(catId)
    }

    override suspend fun insertCategory(category: Category) {
        transactionDao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        transactionDao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        transactionDao.deleteCategory(category)
    }

    override fun getAllPaymentModesStream(): Flow<List<PaymentMode>> {
        return transactionDao.getAllPaymentMode()
    }

    override fun getPaymentModeStream(ptId: Long): Flow<PaymentMode?> {
       return transactionDao.getPaymentMode(ptId)
    }

    override suspend fun insertPaymentMode(paymentMode: PaymentMode): Long {
        return transactionDao.insertPaymentMode(paymentMode)
    }

    override suspend fun updatePaymentMode(paymentMode: PaymentMode) {
        transactionDao.updatePaymentMode(paymentMode)
    }

    override suspend fun deletePaymentMode(paymentMode: PaymentMode) {
        transactionDao.deletePaymentMode(paymentMode)
    }
//Dinesh Changes
    override fun getTotalIncome(): Flow<Double> {
        return transactionDao.getTotalIncome()
            .map { it ?: 0.0 }
    }

    override fun getTotalExpense(): Flow<Double> {
        return transactionDao.getTotalExpense()
            .map { it ?: 0.0 }
    }

    override fun getCurrentMonthTransactions(): Flow<List<Transaction>> {
        return transactionDao.getCurrentMonthTransactions()
    }

    override fun getCategoryWiseExpense(): Flow<List<CategoryExpense>> {
        return transactionDao.getCategoryWiseExpense()
    }



}