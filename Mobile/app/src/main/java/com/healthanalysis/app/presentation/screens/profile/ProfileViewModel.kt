package com.healthanalysis.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.local.TokenManager
import com.healthanalysis.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val email: String = "",
    val initials: String = "",
    val age: Int? = null,
    val height: Int? = null,
    val weight: Double? = null,
    val gender: String? = null,
    val goals: List<String> = listOf(
        "\u0421\u043D\u0438\u0437\u0438\u0442\u044C \u0445\u043E\u043B\u0435\u0441\u0442\u0435\u0440\u0438\u043D",
        "\u041F\u043E\u0432\u044B\u0441\u0438\u0442\u044C \u0436\u0435\u043B\u0435\u0437\u043E",
        "\u041F\u0438\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 \u0432\u043E\u0434\u044B"
    )
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = profileRepository.getProfile()
                result.onSuccess { profile ->
                    val initials = profile.email.take(2).uppercase()
                    _uiState.value = ProfileUiState(
                        isLoading = false,
                        email = profile.email,
                        initials = initials,
                        age = profile.age,
                        height = profile.height,
                        weight = profile.weight,
                        gender = profile.gender
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearTokens()
        }
    }
}
