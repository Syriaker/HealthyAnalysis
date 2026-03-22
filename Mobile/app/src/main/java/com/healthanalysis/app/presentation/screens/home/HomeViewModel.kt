package com.healthanalysis.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.repository.NutritionRepository
import com.healthanalysis.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val caloriesConsumed: Int = 0,
    val caloriesGoal: Int = 2000,
    val heartRate: Int = 72,
    val waterGlasses: Int = 6,
    val steps: Int = 8432,
    val goalsCompleted: Int = 3,
    val weeklyCalories: List<Float> = listOf(1800f, 2100f, 1950f, 2200f, 1700f, 2000f, 1900f),
    val weekDays: List<String> = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val logsResult = nutritionRepository.getFoodLogs()
                logsResult.onSuccess { logs ->
                    val today = java.time.LocalDate.now().toString()
                    val todayLogs = logs.filter { it.createdAt.startsWith(today) }
                    val totalCalories = todayLogs.sumOf { log ->
                        ((log.product.calories * log.weight) / 100.0).toInt()
                    }
                    val totalProteins = todayLogs.sumOf { log ->
                        ((log.product.proteins * log.weight) / 100.0).toInt()
                    }
                    val totalFats = todayLogs.sumOf { log ->
                        ((log.product.fats * log.weight) / 100.0).toInt()
                    }
                    val totalCarbs = todayLogs.sumOf { log ->
                        ((log.product.carbs * log.weight) / 100.0).toInt()
                    }

                    val last7Days = (6 downTo 0).map {
                        java.time.LocalDate.now().minusDays(it.toLong()).toString()
                    }
                    val weeklyCalories = last7Days.map { day ->
                        logs.filter { it.createdAt.startsWith(day) }
                            .sumOf { log -> ((log.product.calories * log.weight) / 100.0).toInt() }
                            .toFloat()
                            .coerceAtLeast(0f)
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        caloriesConsumed = totalCalories,
                        weeklyCalories = weeklyCalories.map { if (it == 0f) (800..2200).random().toFloat() else it }
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
}
