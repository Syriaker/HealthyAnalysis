package com.healthanalysis.app.presentation.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.models.FoodLogResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class FoodUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val caloriesConsumed: Int = 0,
    val caloriesRemaining: Int = 2000,
    val caloriesGoal: Int = 2000,
    val proteins: Int = 0,
    val proteinsGoal: Int = 120,
    val fats: Int = 0,
    val fatsGoal: Int = 65,
    val carbs: Int = 0,
    val carbsGoal: Int = 250,
    val breakfastLogs: List<FoodLogResponse> = emptyList(),
    val lunchLogs: List<FoodLogResponse> = emptyList(),
    val dinnerLogs: List<FoodLogResponse> = emptyList(),
    val snackLogs: List<FoodLogResponse> = emptyList(),
    val breakfastCalories: Int = 0,
    val lunchCalories: Int = 0,
    val dinnerCalories: Int = 0,
    val snackCalories: Int = 0
)

@HiltViewModel
class FoodViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        // GET nutrition/log/ does not exist on the backend — show empty state
        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
