package com.group4.expensi.viewModel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.group4.expensi.ExpensiApplication
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.local.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

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

    fun onDismissDialogs(){
        _uiState.value = _uiState.value.copy(
            showAddCategoryDialog = false,
            showAddPaymentModeDialog = false,
            categoryBeingEdited = null,
            paymentModeBeingEdited = null,
            nameError = null
        )
    }


    fun onAddCategory(name: String){
        val trimmedName=name.trim()
        if (isDuplicateCategoryName(trimmedName)){
            _uiState.value=_uiState.value.copy(
                nameError = "Category already exists"
            )
            return
        }

        viewModelScope.launch {
            repository.insertCategory(
                Category(
                    catId=0,
                    catTitle=trimmedName,//** name
                    catIconUrl = ""

                )
            )
            _uiState.value = _uiState.value.copy(showAddCategoryDialog = false,  nameError = null)
        }
    }

    fun onAddPaymentMode(
        title: String,
        description: String,
        startingBalanceText: String
    ) {

        val trimmedTitle=title.trim()
        if (isDuplicatePaymentModeName(trimmedTitle)){
            _uiState.value=_uiState.value.copy(
                nameError="Payment mode already exists"
            )
            return
        }
        val balance=validateStartingBalance(startingBalanceText)
        if (balance==null) {
            _uiState.value=_uiState.value.copy(
                balanceError="Starting balance must be a +ve number"
            )
            return
        }
        viewModelScope.launch {
            val paymentModeId = repository.insertPaymentMode(
                PaymentMode(
                    ptId = 0,
                    ptTitle = title,
                    ptDescription = description,
                    ptStartingBalance = balance,
                    ptIconUrl = ""
                )
            )
            if (balance > 0f) {
                repository.insertTransaction(
                    Transaction(
                        tId = 0,
                        tTitle = "Opening Balance",
                        tDescription = "Initial balance for $trimmedTitle",
                        tAmount = balance,
                        tDate = Date(),
                        tIsExpense = false,
                        tCategoryId = -1L,
                        tPaymentModeId = paymentModeId
                    )
                )
            }
            _uiState.value = _uiState.value.copy(showAddPaymentModeDialog = false, nameError = null, balanceError= null)
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

    //editing
    fun onEditCategory(category: Category){
        _uiState.value = _uiState.value.copy(
            categoryBeingEdited=category
        )
    }
    fun onEditPaymentMode(paymentMode: PaymentMode){
        _uiState.value=_uiState.value.copy(
            paymentModeBeingEdited=paymentMode
        )
    }

    fun onUpdateCategory(updatedName: String){
        val category=_uiState.value.categoryBeingEdited ?: return
        val trimmedName=updatedName.trim()
        if (isDuplicateCategoryName(trimmedName)) {
            _uiState.value=_uiState.value.copy(
                nameError="Category already exists"
            )
            return
        }

        viewModelScope.launch{
            repository.updateCategory(
                category.copy(catTitle=trimmedName)
            )
            _uiState.value = _uiState.value.copy(categoryBeingEdited = null, nameError = null)
        }
    }
    fun onUpdatePaymentMode(title: String, description: String
    ) {

        val paymentMode=_uiState.value.paymentModeBeingEdited ?: return
        val trimmedTitle=title.trim()

        if (isDuplicatePaymentModeName(trimmedTitle)) {
            _uiState.value=_uiState.value.copy(
                nameError="Payment mode already exists"
            )
            return
        }
        viewModelScope.launch {
            repository.updatePaymentMode(
                paymentMode.copy(
                    ptTitle=title,
                    ptDescription=description
                )
            )
            _uiState.value=_uiState.value.copy(paymentModeBeingEdited=null, nameError=null)
        }
    }

    //Duplicate
    private fun isDuplicateCategoryName(name: String): Boolean{
        val normal=name.trim().lowercase()
        return _uiState.value.categories.any{
            it.catTitle.trim().lowercase()==normal&&
                    it.catId !=_uiState.value.categoryBeingEdited?.catId
        }
    }

    private fun isDuplicatePaymentModeName(name: String): Boolean {
        val normal = name.trim().lowercase()
        return _uiState.value.paymentModes.any {
            it.ptTitle.trim().lowercase() == normal&&
                    it.ptId != _uiState.value.paymentModeBeingEdited?.ptId
        }
    }

    fun onNameChanged(){
        if (_uiState.value.nameError != null) {
            _uiState.value = _uiState.value.copy(nameError = null)
        }
    }
    private fun validateStartingBalance(text: String): Float?{
        if (text.isBlank()) return 0f
        val value=text.toFloatOrNull() ?: return null
        return if(value < 0f) null else value
    }

    fun onBalanceChanged(){
        if (_uiState.value.balanceError !=null){
            _uiState.value=_uiState.value.copy(balanceError = null)
        }
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
