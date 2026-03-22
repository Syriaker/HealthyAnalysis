package com.healthanalysis.app.presentation.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.models.FoodLogResponse
import com.healthanalysis.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
class FoodViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = nutritionRepository.getFoodLogs()
                result.onSuccess { logs ->
                    val today = java.time.LocalDate.now().toString()
                    val todayLogs = logs.filter { it.createdAt.startsWith(today) }

                    val breakfast = todayLogs.filter { it.mealType == "breakfast" }
                    val lunch = todayLogs.filter { it.mealType == "lunch" }
                    val dinner = todayLogs.filter { it.mealType == "dinner" }
                    val snack = todayLogs.filter { it.mealType == "snack" }

                    fun calcCalories(list: List<FoodLogResponse>) =
                        list.sumOf { ((it.product.calories * it.weight) / 100.0).toInt() }
                    fun calcNutrient(list: List<FoodLogResponse>, getter: (FoodLogResponse) -> Double) =
                        list.sumOf { ((getter(it) * it.weight) / 100.0).toInt() }

                    val totalCal = calcCalories(todayLogs)
                    val totalP = calcNutrient(todayLogs) { it.product.proteins }
                    val totalF = calcNutrient(todayLogs) { it.product.fats }
                    val totalC = calcNutrient(todayLogs) { it.product.carbs }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        caloriesConsumed = totalCal,
                        caloriesRemaining = (2000 - totalCal).coerceAtLeast(0),
                        proteins = totalP,
                        fats = totalF,
                        carbs = totalC,
                        breakfastLogs = breakfast,
                        lunchLogs = lunch,
                        dinnerLogs = dinner,
                        snackLogs = snack,
                        breakfastCalories = calcCalories(breakfast),
                        lunchCalories = calcCalories(lunch),
                        dinnerCalories = calcCalories(dinner),
                        snackCalories = calcCalories(snack)
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

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
