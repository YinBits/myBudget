package com.faj.myb.viewmodel

import androidx.lifecycle.ViewModel
import com.faj.myb.model.Transaction
import com.faj.myb.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

data class HomeUiState(
    val balance: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Mock
        _uiState.value = HomeUiState(
            balance = 12000.00,
            recentTransactions = listOf(
                Transaction("Nome do gasto", Date(), -200.00, TransactionType.EXPENSE),
                Transaction("Nome do Entrada", Date(), 100.00, TransactionType.INCOME),
                Transaction("Nome do gasto", Date(), -200.00, TransactionType.EXPENSE),
                Transaction("Nome do Entrada", Date(), 100.00, TransactionType.INCOME),
            )
        )
    }
}