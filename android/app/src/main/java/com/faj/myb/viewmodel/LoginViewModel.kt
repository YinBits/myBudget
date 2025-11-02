package com.faj.myb.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faj.myb.api.RetrofitInstance
import com.faj.myb.api.request.LoginRequest
import com.faj.myb.api.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = LoginUiState.Loading
            try {
                RetrofitInstance.api.login(LoginRequest(email, password))
                    .enqueue(object : retrofit2.Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse?>,
                            response: Response<LoginResponse?>
                        ) {
                            if (response.isSuccessful) {
                                RetrofitInstance.token = response.body()?.token
                                uiState = LoginUiState.Success
                                return
                            } else {
                                uiState = LoginUiState.Error("Failed to login")
                            }
                        }

                        override fun onFailure(
                            call: Call<LoginResponse?>,
                            t: Throwable
                        ) {
                            uiState = LoginUiState.Error("Failed to login")
                        }
                    })
            } catch (e: Exception) {
                LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
