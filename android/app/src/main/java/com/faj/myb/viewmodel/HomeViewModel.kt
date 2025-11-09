package com.faj.myb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faj.myb.api.RetrofitInstance
import com.faj.myb.model.Transaction
import com.faj.myb.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HomeUiState(
    val balance: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun load() = viewModelScope.launch {
        val transactions = RetrofitInstance.api.getTransactions()
        val dashboard = runCatching { RetrofitInstance.api.getDashboard().balance }.getOrNull()

        _uiState.update {
            it.copy(
                balance = dashboard?.toDouble() ?: 0.0,
                recentTransactions = transactions.map { response ->
                    Transaction(
                        response.id,
                        response.description,
                        response.date.run {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            dateFormat.parse(this) ?: Date()
                        },
                        response.amount.toDouble(),
                        TransactionType.valueOf(response.type)
                    )
                }
            )
        }
    }
}
