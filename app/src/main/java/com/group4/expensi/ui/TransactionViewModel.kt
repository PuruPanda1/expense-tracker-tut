package com.group4.expensi.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState : StateFlow<TransactionUiState> = _uiState.asStateFlow()

}