package com.healthanalysis.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.models.ProfileUpdateRequest
import com.healthanalysis.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileSettingsUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,
    val height: String = "",
    val weight: String = "",
    val birthDate: String = "",
    val gender: String = ""
)

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSettingsUiState())
    val uiState: StateFlow<ProfileSettingsUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile()
                .onSuccess { profile ->
                    _uiState.value = ProfileSettingsUiState(
                        isLoading = false,
                        height = profile.height?.toString() ?: "",
                        weight = profile.weight?.let { "%.1f".format(it) } ?: "",
                        birthDate = profile.birthDate ?: "",
                        gender = profile.gender ?: ""
                    )
                }
                .onFailure { e ->
                    _uiState.value = ProfileSettingsUiState(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }

    fun onHeightChanged(value: String) {
        _uiState.value = _uiState.value.copy(height = value)
    }

    fun onWeightChanged(value: String) {
        _uiState.value = _uiState.value.copy(weight = value)
    }

    fun onBirthDateChanged(value: String) {
        _uiState.value = _uiState.value.copy(birthDate = value)
    }

    fun onGenderChanged(value: String) {
        _uiState.value = _uiState.value.copy(gender = value)
    }

    fun save() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            val state = _uiState.value
            val request = ProfileUpdateRequest(
                height = state.height.toIntOrNull(),
                weight = state.weight.toDoubleOrNull(),
                birthDate = state.birthDate.ifBlank { null },
                gender = state.gender.ifBlank { null }
            )
            profileRepository.updateProfile(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "\u041E\u0448\u0438\u0431\u043A\u0430 \u0441\u043E\u0445\u0440\u0430\u043D\u0435\u043D\u0438\u044F"
                    )
                }
        }
    }
}
