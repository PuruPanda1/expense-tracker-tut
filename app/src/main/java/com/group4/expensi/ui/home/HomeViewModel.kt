package com.group4.expensi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.expensi.data.local.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            repository.getTotalIncome(),
            repository.getTotalExpense(),
            repository.getCurrentMonthTransactions(),
            repository.getCategoryWiseExpense()
        ) { income, expense, transactions, categoryExpense ->

            HomeUiState(
                isLoading = false,
                totalIncome = income,
                totalExpense = expense,
                balance = income - expense,
                transactions = transactions,
                categoryExpense = categoryExpense
            )
        }.catch { e ->
            emit(HomeUiState(error = e.message))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}
