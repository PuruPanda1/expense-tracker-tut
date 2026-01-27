package com.group4.expensi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.ui.pages.HomePage
import com.group4.expensi.ui.pages.LoginPage
import com.group4.expensi.ui.pages.SignUpPage

@Composable
fun ExpensiNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ExpensiRoutes.LOGIN.route, builder = {
        composable(ExpensiRoutes.LOGIN.route){
            LoginPage(modifier, navController,authViewModel)
        }

        composable(ExpensiRoutes.SIGNUP.route){
            SignUpPage(modifier, navController,authViewModel)
        }

        composable(ExpensiRoutes.HOME.route){
            HomePage(modifier, navController,authViewModel)
        }
    })
}