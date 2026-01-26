package com.group4.expensi.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.local.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel(){

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState : StateFlow<TransactionUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            transactionRepository
                .getAllTransactionsStream()
                .collect { list ->
                    _uiState.update {
                        it.copy(transactions = list)
                    }
                }
        }
        viewModelScope.launch {
            transactionRepository
                .getAllCategoryStream()
                .collect { categories ->
                    _categoryMap.value = categories.associateBy { it.catId }
                }
        }
        viewModelScope.launch {
            transactionRepository
                .getAllPaymentModesStream()
                .collect { modes ->
                    _paymentModeMap.value = modes.associateBy { it.ptId }
                }
        }
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch {
            transactionRepository.insertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction){
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(transaction)
        }
    }

    private val _categoryMap = MutableStateFlow<Map<Long, Category>>(emptyMap())
    val categoryMap: StateFlow<Map<Long, Category>> = _categoryMap.asStateFlow()
    private val _paymentModeMap =
        MutableStateFlow<Map<Long, PaymentMode>>(emptyMap())

    val paymentModeMap: StateFlow<Map<Long, PaymentMode>> = _paymentModeMap
}