package com.group4.expensi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.group4.expensi.viewModel.auth.AuthViewModel
import com.group4.expensi.navigation.expensiAppNavigation.ExpensiNavigation
import com.group4.expensi.ui.theme.ExpensiTheme
import com.group4.expensi.viewModel.transaction.TransactionViewModel
import com.group4.expensi.viewModel.transaction.TransactionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val authViewModel : AuthViewModel by viewModels()

        val transactionViewModel : TransactionViewModel by viewModels(
            factoryProducer = { TransactionViewModelFactory(
                (this.applicationContext as ExpensiApplication).container.transactionRepository
            ) }
        )
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        setContent {
            ExpensiTheme {
                ExpensiNavigation(
                    authViewModel = authViewModel,
                    transactionViewModel = transactionViewModel
                )
            }
        }
    }
}


