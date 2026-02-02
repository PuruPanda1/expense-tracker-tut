package com.group4.expensi.navigation.expensiAppNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.viewModel.auth.AuthViewModel
import com.group4.expensi.data.local.ExpensiDatabase
import com.group4.expensi.data.local.repository.OfflineTransactionRepository
import com.group4.expensi.viewModel.home.HomeViewModel
import com.group4.expensi.ui.pages.auth.LoginPage
import com.group4.expensi.ui.pages.applicationScreen.MainScreen
import com.group4.expensi.ui.pages.auth.OTPScreen
import com.group4.expensi.ui.pages.auth.PhoneLogin
import com.group4.expensi.ui.pages.auth.SignUpPage
import com.group4.expensi.ui.pages.welcomeScreen.OnboardingScreen
import com.group4.expensi.ui.pages.welcomeScreen.WelcomeScreen
import com.group4.expensi.viewModel.transaction.TransactionViewModel

@Composable
fun ExpensiNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    transactionViewModel: TransactionViewModel
) {
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

    NavHost(
        navController = navController,
        startDestination = ExpensiRoutes.ONBOARDING.route
    ) {
        composable(ExpensiRoutes.ONBOARDING.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(ExpensiRoutes.WELCOME.route) {
                        popUpTo(ExpensiRoutes.ONBOARDING.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(ExpensiRoutes.WELCOME.route) {
            WelcomeScreen(modifier, navController, authViewModel)
        }
        composable(ExpensiRoutes.LOGIN.route) {
            LoginPage(modifier, navController, authViewModel)
        }
        composable(ExpensiRoutes.SIGNUP.route) {
            SignUpPage(modifier, navController, authViewModel)
        }
        composable(ExpensiRoutes.PHONE_LOGIN.route) {
            PhoneLogin(
                modifier = modifier,
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable(ExpensiRoutes.OTP.route) {
            OTPScreen(
                modifier = modifier,
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable(ExpensiRoutes.HOME.route) {
            MainScreen(
                modifier = modifier,
                rootNavController = navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
                transactionViewModel = transactionViewModel
            )
        }
    }
}
