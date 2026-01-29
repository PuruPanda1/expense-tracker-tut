package com.group4.expensi.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.group4.expensi.auth.AuthState
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.navigation.ExpensiRoutes
import com.group4.expensi.ui.components.TopBar
import com.group4.expensi.ui.settings.components.CategoryItem
import com.group4.expensi.ui.settings.components.PaymentModeItem
import com.group4.expensi.ui.settings.components.SettingsSection
import com.group4.expensi.ui.settings.components.AddCategoryDialog
import com.group4.expensi.ui.settings.components.AddPaymentModeDialog
import com.group4.expensi.ui.settings.components.DeleteConfirmationDialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(authViewModel: AuthViewModel, navController: NavController) {
    val settingsViewModel: SettingsViewModel =
        viewModel(factory = SettingsViewModel.Factory)
    val uiState by settingsViewModel.uiState.collectAsState()

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AuthState.UnAuthenticated -> navController.navigate(ExpensiRoutes.LOGIN.route){
                popUpTo(0){inclusive = true}
            }
            else -> Unit
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                actions = {
                    IconButton(
                        onClick = { authViewModel.signOut() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SettingsSection(
                    title = "Categories",
                    description = "Group your expenses",
                    onAddClick = settingsViewModel::onAddCategoryClick
                ) {
                    uiState.categories.forEach { category ->
                        CategoryItem(
                            title = category.catTitle,
                            onDeleteClick = {
                                settingsViewModel.onRequestDeleteCategory(category)
                            }
                        )
                    }
                }
            }
            item {
                SettingsSection(
                    title = "Payment Modes",
                    description = "All payment methods you use",
                    onAddClick = settingsViewModel::onAddPaymentModeClick
                ) {
                    uiState.paymentModes.forEach { paymentMode ->
                        PaymentModeItem(
                            title = paymentMode.ptTitle,
                            description = paymentMode.ptDescription,
                            onDeleteClick = {
                                settingsViewModel.onRequestDeletePaymentMode(paymentMode)
                            }
                        )
                    }
                }
            }
        }
    }
    if (uiState.showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = settingsViewModel::onDismissDialogs,
            onAddCategory = settingsViewModel::onAddCategory
        )
    }
    if (uiState.showAddPaymentModeDialog) {
        AddPaymentModeDialog(
            onDismiss = settingsViewModel::onDismissDialogs,
            onAddPaymentMode = settingsViewModel::onAddPaymentMode
        )
    }
    uiState.categoryToDelete?.let { category ->
        DeleteConfirmationDialog(
            title = "Delete Category",
            message = "Are you sure you want to delete '${category.catTitle}'?",
            onConfirm = settingsViewModel::confirmDeleteCategory,
            onDismiss = settingsViewModel::cancelDelete
        )
    }
    uiState.paymentModeToDelete?.let { paymentMode ->
        DeleteConfirmationDialog(
            title = "Delete Payment Mode",
            message = "Are you sure you want to delete '${paymentMode.ptTitle}'?",
            onConfirm = settingsViewModel::confirmDeletePaymentMode,
            onDismiss = settingsViewModel::cancelDelete
        )
    }
}

