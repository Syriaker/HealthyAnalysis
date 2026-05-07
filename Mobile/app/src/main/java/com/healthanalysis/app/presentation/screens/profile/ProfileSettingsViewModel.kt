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
    val loadError: String? = null,  // ошибка загрузки профиля
    val isSaving: Boolean = false,
    val saveError: String? = null,  // ошибка сохранения
    val saveSuccess: Boolean = false,
    val height: String = "",
    val weight: String = "",
    val birthDate: String = "",
    val gender: String = ""
) {
    // оставляем для обратной совместимости с экраном
    val error: String? get() = saveError
}

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSettingsUiState())
    val uiState: StateFlow<ProfileSettingsUiState> = _uiState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loadError = null)
            profileRepository.getProfile()
                .onSuccess { profile ->
                    _uiState.value = ProfileSettingsUiState(
                        isLoading = false,
                        height = profile.height?.toString() ?: "",
                        weight = profile.weight?.let { "%.1f".format(it) } ?: "",
                        birthDate = profile.birthDate ?: "",
                        gender = normalizeGender(profile.gender)
                    )
                }
                .onFailure { e ->
                    _uiState.value = ProfileSettingsUiState(
                        isLoading = false,
                        loadError = e.message
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

    private fun normalizeGender(raw: String?): String = when (raw?.lowercase()) {
        "m", "male" -> "M"
        "f", "female" -> "F"
        else -> ""
    }

    fun save() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, saveError = null)
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
                        saveError = e.message ?: "\u041E\u0448\u0438\u0431\u043A\u0430 \u0441\u043E\u0445\u0440\u0430\u043D\u0435\u043D\u0438\u044F"
                    )
                }
        }
    }
}
