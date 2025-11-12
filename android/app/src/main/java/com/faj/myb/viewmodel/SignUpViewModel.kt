package com.faj.myb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faj.myb.api.RetrofitInstance
import com.faj.myb.api.request.SignUpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun register(name: String, email: String, pass: String) {
        _uiState.value = RegisterUiState.Loading
        viewModelScope.launch {
            try {
                val request = SignUpRequest(name = name, email = email, password = pass)
                RetrofitInstance.api.register(request)
                _uiState.value = RegisterUiState.Success
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error("Falha ao registrar $e")
            }
        }
    }
}