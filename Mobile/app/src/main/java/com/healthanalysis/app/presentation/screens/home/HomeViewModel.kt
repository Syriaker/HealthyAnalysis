package com.healthanalysis.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val caloriesConsumed: Int = 0,
    val caloriesGoal: Int = 2000,
    val heartRate: Int = 0,
    val waterGlasses: Int = 0,
    val steps: Int = 0,
    val goalsCompleted: Int = 0,
    val weeklyCalories: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f),
    val weekDays: List<String> = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        // GET nutrition/log/ does not exist on the backend — HomeScreen uses placeholder data
    }
}
