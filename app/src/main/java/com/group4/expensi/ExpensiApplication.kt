package com.group4.expensi

import android.app.Application
import com.group4.expensi.data.local.util.AppContainer
import com.group4.expensi.data.local.util.AppDataContainer

class ExpensiApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}