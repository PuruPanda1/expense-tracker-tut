package com.group4.expensi.data.local

import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode

object DefaultData {

    val categories = listOf(
        Category(0L, "Food", "ic_food"),
        Category(0L, "Home", "home"),
        Category(0L, "Shopping", "ic_shopping"),
        Category(0L, "Fuel", "ic_fuel"),
        Category(0L, "Salary", "ic_salary")
    )

    val paymentModes = listOf(
        PaymentMode(0L, "Cash", "Cash in hand", 0f, "ic_cash")
    )
}
