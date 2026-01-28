package com.group4.expensi

import android.app.Application
import com.group4.expensi.data.local.ExpensiDatabase
import com.group4.expensi.data.local.repository.OfflineTransactionRepository
import com.group4.expensi.data.local.repository.TransactionRepository
import com.group4.expensi.data.local.util.AppContainer
import com.group4.expensi.data.local.util.AppDataContainer

class ExpensiApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}


class AppContainer(context: Application) {
    private val database = ExpensiDatabase.getDatabase(context)

    val transactionRepository: TransactionRepository =
        OfflineTransactionRepository(database.transactionDao())
}