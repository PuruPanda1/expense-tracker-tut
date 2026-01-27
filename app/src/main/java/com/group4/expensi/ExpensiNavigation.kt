package com.group4.expensi

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

    NavHost(navController = navController, startDestination = "signup", builder = {
        composable("login"){
            LoginPage(modifier, navController,authViewModel)
        }

        composable("signup"){
            SignUpPage(modifier, navController,authViewModel)
        }

        composable("home"){
            HomePage(modifier, navController,authViewModel)
        }
    })
}