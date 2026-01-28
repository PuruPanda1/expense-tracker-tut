package com.group4.expensi.data.local

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            val database = ExpensiDatabase.getDatabase(context)
            val dao = database.transactionDao()
            if (dao.getCategoryCount() == 0) {
                DefaultData.categories.forEach {
                    dao.insertCategory(it)
                }
            }
            if (dao.getPaymentModeCount() == 0) {
                DefaultData.paymentModes.forEach {
                    dao.insertPaymentMode(it)
                }
            }
        }
    }
}
