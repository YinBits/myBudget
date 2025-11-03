package com.faj.myb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faj.myb.api.RetrofitInstance
import com.faj.myb.api.request.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { LoginUiState.Loading }
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                RetrofitInstance.token = response.token
                _uiState.update { LoginUiState.Success }
            } catch (e: Exception) {
                _uiState.update { LoginUiState.Error(e.message ?: "An unknown error occurred") }
            }
        }
    }
}
