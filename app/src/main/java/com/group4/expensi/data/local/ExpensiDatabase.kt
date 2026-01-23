package com.group4.expensi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.group4.expensi.data.local.converter.Converters
import com.group4.expensi.data.local.dao.TransactionDao
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction

@Database(entities = [Transaction::class, Category::class, PaymentMode::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ExpensiDatabase : RoomDatabase(){
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var Instance: ExpensiDatabase? = null

        fun getDatabase(context: Context): ExpensiDatabase {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(
                        context,
                        ExpensiDatabase::class.java,
                        "expensi_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}