package com.group4.expensi.navigation.transactionNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.group4.expensi.ui.pages.applicationScreen.transaction.EntryTransactionScreenUI
import com.group4.expensi.viewModel.transaction.TransactionViewModel

@Composable
fun EntryTransactionRoute(
    transactionId: Long? = null,
    viewModel: TransactionViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.entryUiState.collectAsState()
    val categories by viewModel.categoryMap.collectAsState()
    val paymentModes by viewModel.paymentModeMap.collectAsState()
    LaunchedEffect(transactionId) {
       viewModel.initEntry(transactionId)
    }
    EntryTransactionScreenUI(
        transactionId = transactionId,
        amount = uiState.amount,
        title = uiState.title,
        description = uiState.description,
        isExpense = uiState.isExpense,
        categories = categories.values.toList(),
        selectedCategory = uiState.selectedCategory,
        paymentModes = paymentModes.values.toList(),
        selectedPaymentMode = uiState.selectedPaymentMode,
        onAmountChange = viewModel::onAmountChange,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onExpenseChange = viewModel::onExpenseChange,
        onCategorySelected = viewModel::onCategorySelected,
        onPaymentModeSelected = viewModel::onPaymentModeSelected,
        onSave = {
            viewModel.saveTransaction(transactionId)
            onBack()
        },
        onBack = onBack,
        selectedDate = uiState.selectedDate,
        onDateSelected = viewModel::onDateSelected
    )
}
