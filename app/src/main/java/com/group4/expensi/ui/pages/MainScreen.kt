package com.group4.expensi.ui.pages

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
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.entrytransaction.EntryTransactionRoute
import com.group4.expensi.navigation.BottomNavigationRoutes
import com.group4.expensi.navigation.ExpensiRoutes
import com.group4.expensi.ui.pages.homePageFragments.HomeScreen
import com.group4.expensi.ui.home.HomeViewModel
import com.group4.expensi.ui.settings.SettingsScreen
import com.group4.expensi.ui.theme.GrayText
import com.group4.expensi.ui.theme.Ivory
import com.group4.expensi.ui.theme.PrimaryBlue
import com.group4.expensi.ui.theme.TranslucentPrimary
import com.group4.expensi.ui.transaction.TransactionViewModel

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

