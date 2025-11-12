package com.faj.myb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faj.myb.api.RetrofitInstance
import com.faj.myb.model.FinancialReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FinancialReportUiState(
    val financialReport: FinancialReport? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class FinancialReportViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FinancialReportUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchFinancialReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val report = RetrofitInstance.api.getFinancialReport()
                _uiState.update {
                    FinancialReportUiState(financialReport = report)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
