package com.group4.expensi.ui.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.navigation.BottomNavigationRoutes
import com.group4.expensi.ui.pages.homePageFragments.HomeScreen
import com.group4.expensi.ui.pages.homePageFragments.TransactionScreen
import com.group4.expensi.ui.settings.SettingsScreen
import com.group4.expensi.ui.theme.GrayText
import com.group4.expensi.ui.theme.Ivory
import com.group4.expensi.ui.theme.PrimaryBlue
import com.group4.expensi.ui.theme.TranslucentPrimary

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel,
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }, content = { padding ->
            NavHostContainer(navController = bottomNavController, padding = padding, authViewModel = authViewModel, rootNavController = rootNavController, homeViewModel = homeViewModel)
        }
    )
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    authViewModel: AuthViewModel,
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel
) {

    NavHost(
        navController = navController,

        startDestination = "home",

        modifier = Modifier.padding(paddingValues = padding),

        builder = {

            composable("home") {
                HomeScreen(homeViewModel = homeViewModel)
            }

            composable("transactions") {
                TransactionScreen()
            }

            composable("settings") {
//                SettingScreen(modifier = Modifier, authViewModel = authViewModel, navController = rootNavController)
                SettingsScreen()
            }
        })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
        containerColor = PrimaryBlue
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavigationRoutes.bottomNavigationItems.forEach { navItem ->

            val selected = currentRoute == navItem.route

            NavigationBarItem(
                selected = selected,

                onClick = {
                    navController.navigate(navItem.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo("home") {
                            saveState = true
                        }
                    }
                },

                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                },

                label = {
                    Text(text = navItem.label)
                },

                alwaysShowLabel = false,

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Ivory,
                    unselectedIconColor = GrayText,
                    selectedTextColor = Ivory,
                    unselectedTextColor = GrayText,
                    indicatorColor = TranslucentPrimary
                )
            )
        }
    }
}
