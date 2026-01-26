package com.group4.expensi

import android.app.Application
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.util.AppContainer
import com.group4.expensi.data.local.util.AppDataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpensiApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        //prepopulateCategories()
        //prepopulatePaymentModes()
    }
    private fun prepopulateCategories() {
        val repository = container.transactionRepository

        CoroutineScope(Dispatchers.IO).launch {
            repository.insertCategory(
                Category(
                    catId = 0,
                    catTitle = "Fuel",
                    catIconUrl = "ic_fuel"
                )
            )
            repository.insertCategory(
                Category(
                    catId = 0,
                    catTitle = "Food",
                    catIconUrl = "ic_fuel"
                )
            )
            repository.insertCategory(
                Category(
                    catId = 0,
                    catTitle = "Rent",
                    catIconUrl = "ic_fuel"
                )
            )
            repository.insertCategory(
                Category(
                    catId = 0,
                    catTitle = "Salary",
                    catIconUrl = "ic_fuel"
                )
            )
        }
    }
    private fun prepopulatePaymentModes() {
        val repository = container.transactionRepository

        CoroutineScope(Dispatchers.IO).launch {

            repository.insertPaymentMode(
                PaymentMode(
                    ptId = 0L,
                    ptTitle = "Cash",
                    ptDescription = "Physical cash",
                    ptStartingBalance = 0f,
                    ptIconUrl = "ic_cash"
                )
            )

            repository.insertPaymentMode(
                PaymentMode(
                    ptId = 0L,
                    ptTitle = "UPI",
                    ptDescription = "UPI / bank apps",
                    ptStartingBalance = 0f,
                    ptIconUrl = "ic_upi"
                )
            )

            repository.insertPaymentMode(
                PaymentMode(
                    ptId = 0L,
                    ptTitle = "Credit Card",
                    ptDescription = "Credit card payments",
                    ptStartingBalance = 0f,
                    ptIconUrl = "ic_credit_card"
                )
            )

            repository.insertPaymentMode(
                PaymentMode(
                    ptId = 0L,
                    ptTitle = "Debit Card",
                    ptDescription = "Debit card payments",
                    ptStartingBalance = 0f,
                    ptIconUrl = "ic_debit_card"
                )
            )
        }
    }
}