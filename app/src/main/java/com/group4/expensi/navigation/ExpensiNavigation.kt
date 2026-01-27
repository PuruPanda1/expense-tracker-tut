package com.group4.expensi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.entrytransaction.EntryTransactionRoute
import com.group4.expensi.ui.pages.HomePage
import com.group4.expensi.ui.pages.LoginPage
import com.group4.expensi.ui.pages.SignUpPage
import com.group4.expensi.ui.transaction.TransactionViewModel
import com.group4.expensi.ui.pages.EntryTransactionScreenUI
import com.group4.expensi.ui.pages.TransactionListScreen

@Composable
fun ExpensiNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, transactionViewModel: TransactionViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ExpensiRoutes.HOME.route, builder = {
        composable(ExpensiRoutes.LOGIN.route){
            LoginPage(modifier, navController,authViewModel)
        }

        composable(ExpensiRoutes.SIGNUP.route){
            SignUpPage(modifier, navController,authViewModel)
        }

        composable(ExpensiRoutes.HOME.route){
            HomePage(modifier, navController,authViewModel)
        }
        composable(ExpensiRoutes.TRANSACTION_LIST.route){
            TransactionListScreen(
                viewModel = transactionViewModel,
                onAddClick = {
                    navController.navigate(ExpensiRoutes.TRANSACTION_ADD.route)
                },
                onEditClick = { transaction ->
                    navController.navigate("${ExpensiRoutes.TRANSACTION_EDIT.route}/${transaction.tId}")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(ExpensiRoutes.TRANSACTION_ADD.route) {
            EntryTransactionRoute (
                viewModel = transactionViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = "${ExpensiRoutes.TRANSACTION_EDIT.route}/{transactionId}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")?.toLong()

            EntryTransactionRoute (
                transactionId = transactionId,
                viewModel = transactionViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    })
}