package com.group4.expensi.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.local.repository.TransactionRepository
import com.group4.expensi.entrytransaction.EntryTransactionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

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

    private val _categoryMap = MutableStateFlow<Map<Long, Category>>(emptyMap())
    val categoryMap: StateFlow<Map<Long, Category>> = _categoryMap.asStateFlow()
    private val _paymentModeMap =
        MutableStateFlow<Map<Long, PaymentMode>>(emptyMap())

    val paymentModeMap: StateFlow<Map<Long, PaymentMode>> = _paymentModeMap
    private val _entryUiState = MutableStateFlow(EntryTransactionUiState())
    val entryUiState: StateFlow<EntryTransactionUiState> = _entryUiState.asStateFlow()
    fun onAmountChange(value: String) {
        _entryUiState.update { it.copy(amount = value) }
    }

    fun onTitleChange(value: String) {
        _entryUiState.update { it.copy(title = value) }
    }

    fun onDescriptionChange(value: String) {
        _entryUiState.update { it.copy(description = value) }
    }

    fun onExpenseChange(value: Boolean) {
        _entryUiState.update { it.copy(isExpense = value) }
    }

    fun onCategorySelected(category: Category) {
        _entryUiState.update { it.copy(selectedCategory = category) }
    }

    fun onPaymentModeSelected(mode: PaymentMode) {
        _entryUiState.update { it.copy(selectedPaymentMode = mode) }
    }
    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            val transaction = transactionRepository.getTransactionById(transactionId)

            _entryUiState.value = EntryTransactionUiState(
                amount = transaction.tAmount.toString(),
                title = transaction.tTitle,
                description = transaction.tDescription,
                isExpense = transaction.tIsExpense,
                selectedCategory = _categoryMap.value[transaction.tCategoryId],
                selectedPaymentMode = _paymentModeMap.value[transaction.tPaymentModeId]
            )
        }
    }
    fun resetEntryState() {
        _entryUiState.value = EntryTransactionUiState()
    }
    fun saveTransaction(editingTransactionId: Long? = null) {
        val state = _entryUiState.value

        if (state.title.isBlank() || state.amount.isBlank()) return

        viewModelScope.launch {
            val transaction = Transaction(
                tId = editingTransactionId ?: 0L,
                tTitle = state.title,
                tDescription = state.description,
                tAmount = state.amount.toFloatOrNull() ?: 0f,
                tDate = state.selectedDate,
                tIsExpense = state.isExpense,
                tCategoryId = state.selectedCategory?.catId ?: 1L,
                tPaymentModeId = state.selectedPaymentMode?.ptId ?: 1L
            )

            if (editingTransactionId == null) {
                transactionRepository.insertTransaction(transaction)
            } else {
                transactionRepository.updateTransaction(transaction)
            }

            resetEntryState()
        }
    }
    fun onDateSelected(date: Date) {
        _entryUiState.update {
            it.copy(selectedDate = date)
        }
    }
}