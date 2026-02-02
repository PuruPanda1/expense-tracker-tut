package com.group4.expensi.ui.pages.applicationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.viewModel.auth.AuthViewModel
import com.group4.expensi.navigation.transactionNavigation.EntryTransactionRoute
import com.group4.expensi.navigation.bottomNavigation.BottomNavigationRoutes
import com.group4.expensi.navigation.expensiAppNavigation.ExpensiRoutes
import com.group4.expensi.ui.pages.applicationScreen.home.HomeScreen
import com.group4.expensi.ui.pages.applicationScreen.settings.SettingsScreen
import com.group4.expensi.viewModel.home.HomeViewModel
import com.group4.expensi.ui.pages.applicationScreen.transaction.TransactionListScreen
import com.group4.expensi.viewModel.transaction.TransactionViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel,
    transactionViewModel: TransactionViewModel,
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }, content = { padding ->
            NavHostContainer(navController = bottomNavController, padding = padding, authViewModel = authViewModel, rootNavController = rootNavController, homeViewModel = homeViewModel, transactionViewModel = transactionViewModel)
        }
    )
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    authViewModel: AuthViewModel,
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel,
    transactionViewModel: TransactionViewModel
) {

    NavHost(
        navController = navController,

        startDestination = "home",

        modifier = Modifier.padding(paddingValues = padding),

        builder = {

            composable("home") {
                HomeScreen(homeViewModel = homeViewModel)
            }

            composable("transactions"){
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

            composable("settings") {
//                SettingScreen(modifier = Modifier, authViewModel = authViewModel, navController = rootNavController)
                SettingsScreen(authViewModel = authViewModel, navController = rootNavController)
            }
        })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp
    ) {
        BottomNavigationRoutes.bottomNavigationItems.forEach { navItem ->

            val selected = currentRoute == navItem.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(navItem.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(ExpensiRoutes.HOME.route) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label,
                            modifier = Modifier.size(22.dp)
                        )
                        if (selected) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(18.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = navItem.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

