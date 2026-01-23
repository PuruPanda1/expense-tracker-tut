package com.group4.expensi.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

}