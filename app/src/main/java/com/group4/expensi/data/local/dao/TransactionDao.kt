package com.group4.expensi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.model.CategoryExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * from transactions ORDER BY tDate ASC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * from transactions WHERE tId=:tId")
    fun getTransaction(tId:Long): Flow<Transaction>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * from categories ORDER BY catTitle ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * from categories WHERE catId = :catId")
    fun getCategory(catId: Long): Flow<Category>

    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * from payment_modes ORDER BY ptId ASC")
    fun getAllPaymentMode(): Flow<List<PaymentMode>>

    @Query("SELECT * from payment_modes WHERE ptId = :ptId")
    fun getPaymentMode(ptId: Long): Flow<PaymentMode>

    @Insert
    suspend fun insertPaymentMode(paymentMode: PaymentMode)

    @Update
    suspend fun updatePaymentMode(paymentMode: PaymentMode)

    @Delete
    suspend fun deletePaymentMode(paymentMode: PaymentMode)

    //Dinesh Added
    @Query("""
    SELECT c.catTitle AS catTitle, SUM(t.tAmount) AS total
    FROM transactions t
    INNER JOIN categories c ON t.tCategoryId = c.catId
    WHERE t.tIsExpense = 1
    GROUP BY t.tCategoryId
""")
    fun getCategoryWiseExpense(): Flow<List<CategoryExpense>>

    @Query("""
    SELECT SUM(tAmount) FROM transactions 
    WHERE tIsExpense = 0
""")
    fun getTotalIncome(): Flow<Double?>

    @Query("""
    SELECT SUM(tAmount) FROM transactions 
    WHERE tIsExpense = 1
""")
    fun getTotalExpense(): Flow<Double?>

    @Query("""
    SELECT * FROM transactions 
    WHERE strftime('%m', tDate/1000, 'unixepoch') = strftime('%m', 'now')
      AND strftime('%Y', tDate/1000, 'unixepoch') = strftime('%Y', 'now')
    ORDER BY tDate DESC
""")
    fun getCurrentMonthTransactions(): Flow<List<Transaction>>
}