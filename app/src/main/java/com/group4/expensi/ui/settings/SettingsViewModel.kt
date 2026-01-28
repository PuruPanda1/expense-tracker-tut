package com.group4.expensi.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.group4.expensi.ExpensiApplication
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState=MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> =_uiState.asStateFlow()

    init {
        observeCategories()
        observePaymentModes()
    }
    private fun observeCategories() {
        viewModelScope.launch {
            repository.getAllCategoryStream().collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }
    }
    private fun observePaymentModes(){
        viewModelScope.launch {
            repository.getAllPaymentModesStream().collect { paymentModes ->
                _uiState.value = _uiState.value.copy(paymentModes = paymentModes)
            }
        }
    }

    fun onAddCategoryClick(){
        _uiState.value = _uiState.value.copy(showAddCategoryDialog = true)
    }

    fun onAddPaymentModeClick() {
        _uiState.value = _uiState.value.copy(showAddPaymentModeDialog = true)
    }

    fun onDismissDialogs() {
        _uiState.value = _uiState.value.copy(
            showAddCategoryDialog = false,
            showAddPaymentModeDialog = false
        )
    }

    fun onAddCategory(name: String){
        viewModelScope.launch {
            repository.insertCategory(
                Category(
                    catId = 0,
                    catTitle = name,
                    catIconUrl = ""
                )
            )
            _uiState.value = _uiState.value.copy(showAddCategoryDialog = false)
        }
    }

    fun onAddPaymentMode(
        title: String,
        description: String,
        startingBalance: Float
    ) {
        viewModelScope.launch {
            repository.insertPaymentMode(
                PaymentMode(
                    ptId = 0,
                    ptTitle = title,
                    ptDescription = description,
                    ptStartingBalance = startingBalance,
                    ptIconUrl = ""
                )
            )
            _uiState.value = _uiState.value.copy(showAddPaymentModeDialog = false)
        }
    }

    fun onRequestDeleteCategory(category: Category) {
        _uiState.value = _uiState.value.copy(categoryToDelete = category)
    }

    fun onRequestDeletePaymentMode(paymentMode: PaymentMode) {
        _uiState.value = _uiState.value.copy(paymentModeToDelete = paymentMode)
    }

    fun confirmDeleteCategory() {
        val category = _uiState.value.categoryToDelete ?: return
        viewModelScope.launch {
            repository.deleteCategory(category)
            _uiState.value = _uiState.value.copy(categoryToDelete = null)
        }
    }

    fun confirmDeletePaymentMode() {
        val paymentMode = _uiState.value.paymentModeToDelete ?: return
        viewModelScope.launch {
            repository.deletePaymentMode(paymentMode)
            _uiState.value = _uiState.value.copy(paymentModeToDelete = null)
        }
    }

    fun cancelDelete() {
        _uiState.value = _uiState.value.copy(
            categoryToDelete = null,
            paymentModeToDelete = null
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory=viewModelFactory{
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as ExpensiApplication

                SettingsViewModel(
                    repository=application.container.transactionRepository
                )
            }
        }
    }
}
