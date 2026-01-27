package com.group4.expensi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.data.local.ExpensiDatabase
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.local.repository.OfflineTransactionRepository
import com.group4.expensi.ui.pages.HomePage
import com.group4.expensi.ui.pages.HomeViewModel
import com.group4.expensi.ui.pages.LoginPage
import com.group4.expensi.ui.pages.SignUpPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun ExpensiNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val repository = remember {
        OfflineTransactionRepository(
            ExpensiDatabase.getDatabase(context).transactionDao()
        )
    }

    LaunchedEffect(Unit) {
        insertDummyDataOnce(repository)
    }

    val homeViewModel = remember {
        HomeViewModel(repository)
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }

        composable("signup") {
            SignUpPage(modifier, navController, authViewModel)
        }

        composable("home") {
            HomePage(homeViewModel = homeViewModel)
        }
    }
}

/* -------------------- DUMMY DATA -------------------- */

fun insertDummyDataOnce(repository: OfflineTransactionRepository) {
    CoroutineScope(Dispatchers.IO).launch {

        // ✅ INSERT CATEGORIES FIRST
        repository.insertCategory(Category(1, "Income", "ic_income"))
        repository.insertCategory(Category(2, "Groceries", "ic_groceries"))
        repository.insertCategory(Category(3, "Rent", "ic_rent"))

        // ✅ INSERT TRANSACTIONS
        repository.insertTransaction(
            Transaction(
                tTitle = "Salary",
                tDescription = "Monthly salary",
                tAmount = 50000.0f,
                tDate = Date(),
                tIsExpense = false,
                tPaymentModeId = 1,
                tCategoryId = 1
            )
        )

        repository.insertTransaction(
            Transaction(
                tTitle = "Groceries",
                tDescription = "Supermarket shopping",
                tAmount = 2500.0f,
                tDate = Date(),
                tIsExpense = true,
                tPaymentModeId = 1,
                tCategoryId = 2
            )
        )

        repository.insertTransaction(
            Transaction(
                tTitle = "Rent",
                tDescription = "House rent",
                tAmount = 12000.0f,
                tDate = Date(),
                tIsExpense = true,
                tPaymentModeId = 1,
                tCategoryId = 3
            )
        )
    }
}

