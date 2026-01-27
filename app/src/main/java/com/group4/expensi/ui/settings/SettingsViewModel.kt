package com.group4.expensi.ui.settings

import androidx.lifecycle.ViewModel
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class SettingsViewModel : ViewModel(){
    private val _uiState=MutableStateFlow(
        SettingsUiState(
            categories = listOf(
                Category(1, "Food", ""),
                Category(2, "Travel", ""),
                Category(3, "Shopping", "")
            ),
            paymentModes =listOf(
                PaymentMode(1, "Cash", "Physical cash", 0f, ""),
                PaymentMode(2, "UPI", "Digital payment", 0f, "")
            )
        )
    )
    val uiState: StateFlow<SettingsUiState> =_uiState.asStateFlow()
    fun onAddCategoryClick() {
        _uiState.value =_uiState.value.copy(showAddCategoryDialog = true)
    }
    fun onAddPaymentModeClick() {
        _uiState.value =_uiState.value.copy(showAddPaymentModeDialog = true)
    }

    fun onDismissDialogs() {
        _uiState.value =_uiState.value.copy(
            showAddCategoryDialog = false,
            showAddPaymentModeDialog = false
        )
    }

    fun onDeleteCategory(category: Category) {
        _uiState.value =_uiState.value.copy(
            categories =_uiState.value.categories.filterNot { it.catId == category.catId }
        )
    }

    fun onDeletePaymentMode(paymentMode: PaymentMode) {
        _uiState.value =_uiState.value.copy(
            paymentModes =_uiState.value.paymentModes.filterNot { it.ptId == paymentMode.ptId }
        )
    }

    fun onAddCategory(name: String) {
        val newCategory =Category(
            catId =System.currentTimeMillis(),
            catTitle =name,
            catIconUrl =""
        )
        _uiState.value =_uiState.value.copy(
            categories =_uiState.value.categories + newCategory,
            showAddCategoryDialog =false
        )
    }
    fun onAddPaymentMode(
        title: String,
        description: String,
        startingBalance: Float
    ) {
        val newPaymentMode=PaymentMode(
            ptId=System.currentTimeMillis(),
            ptTitle=title,
            ptDescription=description,
            ptStartingBalance=startingBalance,
            ptIconUrl=""
        )

        _uiState.value=_uiState.value.copy(
            paymentModes=_uiState.value.paymentModes + newPaymentMode,
            showAddPaymentModeDialog=false
        )
    }
    fun onRequestDeleteCategory(category: Category) {
        _uiState.value=_uiState.value.copy(categoryToDelete = category)
    }

    fun onRequestDeletePaymentMode(paymentMode: PaymentMode) {
        _uiState.value=_uiState.value.copy(paymentModeToDelete = paymentMode)
    }
    fun confirmDeleteCategory() {
        val category=_uiState.value.categoryToDelete ?: return
        _uiState.value=_uiState.value.copy(
            categories=_uiState.value.categories.filterNot { it.catId == category.catId },
            categoryToDelete=null
        )
    }

    fun confirmDeletePaymentMode() {
        val paymentMode=_uiState.value.paymentModeToDelete ?: return

        _uiState.value=_uiState.value.copy(
            paymentModes=_uiState.value.paymentModes.filterNot { it.ptId == paymentMode.ptId },
            paymentModeToDelete=null
        )
    }
    fun cancelDelete() {
        _uiState.value=_uiState.value.copy(
            categoryToDelete=null,
            paymentModeToDelete=null
        )
    }


}
