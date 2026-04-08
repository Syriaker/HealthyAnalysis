package com.healthanalysis.app.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var code by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            authRepository.login(email, password)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            authRepository.register(email, password)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun verify(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            authRepository.verify(email, code)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }
}
