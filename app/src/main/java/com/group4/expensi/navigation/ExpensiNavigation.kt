package com.group4.expensi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.data.local.ExpensiDatabase
import com.group4.expensi.data.local.repository.OfflineTransactionRepository
import com.group4.expensi.ui.home.HomeViewModel
import com.group4.expensi.ui.pages.MainScreen
import com.group4.expensi.ui.pages.LoginPage
import com.group4.expensi.ui.pages.OTPScreen
import com.group4.expensi.ui.pages.PhoneLogin
import com.group4.expensi.ui.pages.SignUpPage
import com.group4.expensi.ui.transaction.TransactionViewModel

@Composable
fun ExpensiNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, transactionViewModel: TransactionViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val repository = remember {
        OfflineTransactionRepository(
            ExpensiDatabase.getDatabase(context).transactionDao()
        )
    }

    val homeViewModel = remember {
        HomeViewModel(repository)
    }

    NavHost(navController = navController, startDestination = ExpensiRoutes.PHONE_LOGIN.route, builder = {
        composable(ExpensiRoutes.LOGIN.route){
            LoginPage(modifier, navController,authViewModel)
        }

        composable(ExpensiRoutes.SIGNUP.route){
            SignUpPage(modifier, navController,authViewModel)
        }

        composable(ExpensiRoutes.PHONE_LOGIN.route){
            PhoneLogin(modifier, authViewModel = authViewModel,navController = navController)
        }

        composable(ExpensiRoutes.OTP.route){
            OTPScreen(modifier, authViewModel = authViewModel,navController = navController)
        }

        composable(ExpensiRoutes.HOME.route){
            MainScreen(modifier = modifier,rootNavController = navController, authViewModel = authViewModel, homeViewModel = homeViewModel, transactionViewModel = transactionViewModel)
        }

    })
}