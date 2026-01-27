package com.group4.expensi.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group4.expensi.ui.settings.components.CategoryItem
import com.group4.expensi.ui.settings.components.PaymentModeItem
import com.group4.expensi.ui.settings.components.SettingsSection
import com.group4.expensi.ui.settings.components.AddCategoryDialog
import com.group4.expensi.ui.settings.components.AddPaymentModeDialog
import com.group4.expensi.ui.settings.components.DeleteConfirmationDialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    Scaffold(
        containerColor=MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title={
                    Text("Settings")
                } ,
                colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
            )
        }
    ) {

        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {

            item {
                SettingsSection(
                    title = "Categories",
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
            title="Delete Category",
            message="Are you sure you want to delete '${category.catTitle}'?",
            onConfirm=settingsViewModel::confirmDeleteCategory,
            onDismiss=settingsViewModel::cancelDelete
        )
    }

    uiState.paymentModeToDelete?.let { paymentMode ->
        DeleteConfirmationDialog(
            title="Delete Payment Mode",
            message="Are you sure you want to delete '${paymentMode.ptTitle}'?",
            onConfirm=settingsViewModel::confirmDeletePaymentMode,
            onDismiss=settingsViewModel::cancelDelete
        )
    }
}
