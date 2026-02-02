package com.group4.expensi.ui.pages.applicationScreen.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.group4.expensi.viewModel.auth.AuthState
import com.group4.expensi.viewModel.auth.AuthViewModel
import com.group4.expensi.navigation.expensiAppNavigation.ExpensiRoutes
import com.group4.expensi.ui.pages.applicationScreen.settings.components.CategoryDialog
import com.group4.expensi.ui.pages.applicationScreen.settings.components.CategoryItem
import com.group4.expensi.ui.pages.applicationScreen.settings.components.PaymentModeDialog
import com.group4.expensi.viewModel.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(authViewModel: AuthViewModel, navController: NavController) {
    val settingsViewModel: SettingsViewModel =
        viewModel(factory = SettingsViewModel.Companion.Factory)
    val uiState by settingsViewModel.uiState.collectAsState()

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

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
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            expanded = false
                            authViewModel.signOut()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout"
                            )
                        }
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
                _root_ide_package_.com.group4.expensi.ui.pages.applicationScreen.settings.components.SettingsSection(
                    title = "Categories",
                    description = "Group your expenses",
                    onAddClick = settingsViewModel::onAddCategoryClick
                ) {
                    uiState.categories.forEach { category ->
                        CategoryItem(
                            title = category.catTitle,
                            onDeleteClick = {
                                settingsViewModel.onRequestDeleteCategory(category)
                            },
                            onEditClick={
                                settingsViewModel.onEditCategory(category)
                            }
                        )
                    }
                }
            }
            item {
                _root_ide_package_.com.group4.expensi.ui.pages.applicationScreen.settings.components.SettingsSection(
                    title = "Payment Modes",
                    description = "All payment methods you use",
                    onAddClick = settingsViewModel::onAddPaymentModeClick
                ) {
                    uiState.paymentModes.forEach { paymentMode ->
                        _root_ide_package_.com.group4.expensi.ui.pages.applicationScreen.settings.components.PaymentModeItem(
                            title = paymentMode.ptTitle,
                            description = paymentMode.ptDescription,
                            onDeleteClick = {
                                settingsViewModel.onRequestDeletePaymentMode(paymentMode)
                            },
                            onEditClick={
                                settingsViewModel.onEditPaymentMode(paymentMode)
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.showAddCategoryDialog) {
        _root_ide_package_.com.group4.expensi.ui.pages.applicationScreen.settings.components.CategoryDialog(
            title = "Add Category",
            initialValue = "",
            confirmText = "Add",
            onDismiss = settingsViewModel::onDismissDialogs,
            onConfirm = settingsViewModel::onAddCategory,
            errorMessage=uiState.nameError,
            onNameChange = settingsViewModel::onNameChanged
        )
    }
    uiState.categoryBeingEdited?.let{category ->
        CategoryDialog(
            title="Edit Category",
            initialValue=category.catTitle,
            confirmText="Save",
            onDismiss=settingsViewModel::onDismissDialogs,
            onConfirm=settingsViewModel::onUpdateCategory,
            errorMessage = uiState.nameError,
            onNameChange = settingsViewModel::onNameChanged

        )
    }

    if (uiState.showAddPaymentModeDialog) {
        PaymentModeDialog(
            title="Add Payment Mode",
            initialName = "",
            initialDescription = "",
            isEditMode = false,
            onDismiss = settingsViewModel::onDismissDialogs,
            onConfirm = settingsViewModel::onAddPaymentMode,
            errorMessage = uiState.nameError,
            onNameChange = settingsViewModel::onNameChanged,
            errorBalanceMessage = uiState.balanceError,
            onBalanceChange = settingsViewModel::onBalanceChanged

        )
    }
    uiState.paymentModeBeingEdited?.let { paymentMode ->
        PaymentModeDialog(
            title = "Edit Payment Mode",
            initialName = paymentMode.ptTitle,
            initialDescription = paymentMode.ptDescription,
            isEditMode = true,
            onDismiss = settingsViewModel::onDismissDialogs,
            onConfirm = { name, description, _ ->
                settingsViewModel.onUpdatePaymentMode(
                    title = name,
                    description = description
                )
            }
            ,errorMessage = uiState.nameError,
            onNameChange = settingsViewModel::onNameChanged,
            errorBalanceMessage = uiState.balanceError,
            onBalanceChange=settingsViewModel::onBalanceChanged

        )
    }


    uiState.categoryToDelete?.let { category ->
        _root_ide_package_.com.group4.expensi.ui.pages.applicationScreen.settings.components.DeleteConfirmationDialog(
            title = "Delete Category",
            message = "Are you sure you want to delete '${category.catTitle}'?",
            onConfirm = settingsViewModel::confirmDeleteCategory,
            onDismiss = settingsViewModel::cancelDelete
        )
    }

    uiState.paymentModeToDelete?.let { paymentMode ->
        _root_ide_package_.com.group4.expensi.ui.pages.applicationScreen.settings.components.DeleteConfirmationDialog(
            title = "Delete Payment Mode",
            message = "Are you sure you want to delete '${paymentMode.ptTitle}'?",
            onConfirm = settingsViewModel::confirmDeletePaymentMode,
            onDismiss = settingsViewModel::cancelDelete
        )
    }

}

