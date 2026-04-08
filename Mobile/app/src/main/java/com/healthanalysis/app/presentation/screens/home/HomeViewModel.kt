package com.healthanalysis.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val caloriesConsumed: Int = 0,
    val heartRate: Int = 0,
    val waterGlasses: Int = 0,
    val steps: Int = 0,
    val weeklyCalories: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f),
    val weekDays: List<String> = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        // No GET endpoint for nutrition logs — HomeScreen shows zeroed data
    }
}
