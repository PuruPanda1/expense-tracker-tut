package com.group4.expensi.navigation.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings

object BottomNavigationRoutes {
    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavigationItem(
            label = "Transactions",
            icon = Icons.Filled.Menu,
            route = "transactions"
        ),
        BottomNavigationItem(
            label = "Settings",
            icon = Icons.Filled.Settings,
            route = "settings"
        ),
    )
}