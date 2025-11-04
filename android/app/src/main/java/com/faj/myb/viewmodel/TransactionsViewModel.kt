package com.faj.myb.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faj.myb.api.RetrofitInstance
import com.faj.myb.api.response.TransactionResponse
import com.faj.myb.model.Transaction
import com.faj.myb.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TransactionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchTransactions()
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                _uiState.update {
                    TransactionsUiState(
                        transactions = RetrofitInstance.api.getTransactions().mapToDomain()
                    )
                }
            } catch (e: Exception) {
                Log.d("YSGS", e.message.toString())
                _uiState.update { it.copy(isLoading = false, error = "Falha ao carregar transações") }
            }
        }
    }

    private fun List<TransactionResponse>.mapToDomain(): List<Transaction> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return this.map {
            Transaction(
                name = it.description,
                date = dateFormat.parse(it.date) ?: java.util.Date(),
                value = it.amount.toDouble(),
                type = TransactionType.valueOf(it.type)
            )
        }
    }
}