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
        enableEdgeToEdge()
        val authViewModel : AuthViewModel by viewModels()

        val transactionViewModel : TransactionViewModel by viewModels(
            factoryProducer = { TransactionViewModelFactory(
                (this.applicationContext as ExpensiApplication).container.transactionRepository
            ) }
        )

        setContent {
            ExpensiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpensiNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel,
                        transactionViewModel = transactionViewModel
                    )
                }
//                SettingsScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun AddTransactionScreen(
    innerPadding: PaddingValues,
    viewModel: TransactionViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {

                val transaction = Transaction(
                    tTitle = title,
                    tDescription = description,
                    tAmount = amount.toFloatOrNull() ?: 0f,
                    tDate = Date(),
                    tIsExpense = true,
                    tPaymentModeId = 1L,
                    tCategoryId = 1L
                )

                viewModel.addTransaction(transaction)
            }
        ) {
            Text("Save Transaction")
        }
    }
}
