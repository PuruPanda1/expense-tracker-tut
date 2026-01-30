package com.group4.expensi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.navigation.ExpensiNavigation
import com.group4.expensi.ui.theme.ExpensiTheme
import com.group4.expensi.ui.transaction.TransactionViewModel
import com.group4.expensi.ui.transaction.TransactionViewModelFactory
import java.util.Date

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


