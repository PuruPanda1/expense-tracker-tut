package com.group4.expensi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.ui.theme.ExpensiTheme
import com.group4.expensi.ui.transaction.TransactionViewModel
import com.group4.expensi.ui.transaction.TransactionViewModelFactory
import com.group4.expensi.ui.transaction.screen.EntryTransactionScreenUI
import com.group4.expensi.ui.transaction.screen.TransactionListScreen
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpensiTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                    AddTransactionScreen(
//                        innerPadding,
//                        viewModel(
//                            factory = TransactionViewModelFactory(
//                                (LocalContext.current.applicationContext
//                                        as ExpensiApplication)
//                                    .container
//                                    .transactionRepository
//                            )
//                        )
//                    )
//                TransactionListScreen(
//                    viewModel(
//                        factory = TransactionViewModelFactory(
//                            (LocalContext.current.applicationContext as ExpensiApplication)
//                                .container
//                                .transactionRepository
//                        )
//                    )
//                )
                EntryTransactionScreenUI()
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
