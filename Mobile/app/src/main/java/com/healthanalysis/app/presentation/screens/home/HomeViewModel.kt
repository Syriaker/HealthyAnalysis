package com.healthanalysis.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val caloriesConsumed: Int = 0,
    val waterMl: Int = 0,
    val weeklyCalories: List<Float> = List(7) { 0f },
    val weekDays: List<String> = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"),
    val showWaterHistory: Boolean = false,
    val selectedWaterDate: LocalDate = LocalDate.now(),
    val waterByDate: Map<String, Int> = emptyMap()
) {
    val selectedWaterMl: Int get() = waterByDate[selectedWaterDate.toString()] ?: 0
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData(showFullLoader = true)
    }

    fun loadData(showFullLoader: Boolean = false) {
        viewModelScope.launch {
            if (showFullLoader) {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            try {
                coroutineScope {
                    val today = LocalDate.now()
                    val monday = today.with(DayOfWeek.MONDAY)

                    val weekJobs = (0..6).map { i ->
                        val date = monday.plusDays(i.toLong())
                        async { date to nutritionRepository.getFoodLogs(date.toString()) }
                    }
                    val waterJob = async { nutritionRepository.getWater(today.toString()) }

                    val weekResults = weekJobs.map { it.await() }
                    val waterResult = waterJob.await()

                    val weeklyCalories = weekResults.map { (_, result) ->
                        result.getOrNull()
                            ?.sumOf { log -> (log.product.calories * log.weight / 100.0).toInt() }
                            ?.toFloat() ?: 0f
                    }

                    val todayCalories = weekResults
                        .firstOrNull { (date, _) -> date == today }
                        ?.second?.getOrNull()
                        ?.sumOf { log -> (log.product.calories * log.weight / 100.0).toInt() }
                        ?: 0

                    val waterMl = waterResult.getOrNull()?.amount ?: 0

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        caloriesConsumed = todayCalories,
                        waterMl = waterMl,
                        weeklyCalories = weeklyCalories,
                        waterByDate = _uiState.value.waterByDate + (today.toString() to waterMl)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }

    fun addWater(amount: Int) {
        val current = _uiState.value
        val dateStr = current.selectedWaterDate.toString()
        val safeAmount = if (amount < 0) maxOf(amount, -current.selectedWaterMl) else amount
        if (safeAmount == 0) return

        viewModelScope.launch {
            nutritionRepository.addWater(safeAmount, dateStr)
                .onSuccess { response ->
                    val c = _uiState.value
                    val isToday = c.selectedWaterDate == LocalDate.now()
                    _uiState.value = c.copy(
                        waterByDate = c.waterByDate + (dateStr to response.amount),
                        waterMl = if (isToday) response.amount else c.waterMl
                    )
                }
        }
    }

    fun showWaterHistory() {
        _uiState.value = _uiState.value.copy(showWaterHistory = true)
    }

    fun hideWaterHistory() {
        _uiState.value = _uiState.value.copy(showWaterHistory = false, selectedWaterDate = LocalDate.now())
    }

    fun selectWaterDate(date: LocalDate) {
        if (date.isAfter(LocalDate.now())) return
        _uiState.value = _uiState.value.copy(selectedWaterDate = date)
        val dateStr = date.toString()
        if (!_uiState.value.waterByDate.containsKey(dateStr)) {
            viewModelScope.launch {
                nutritionRepository.getWater(dateStr)
                    .onSuccess { response ->
                        _uiState.value = _uiState.value.copy(
                            waterByDate = _uiState.value.waterByDate + (dateStr to response.amount)
                        )
                    }
            }
        }
    }

    fun previousWaterWeek() {
        selectWaterDate(_uiState.value.selectedWaterDate.minusWeeks(1))
    }

    fun nextWaterWeek() {
        val current = _uiState.value
        val today = LocalDate.now()
        val newDate = current.selectedWaterDate.plusWeeks(1)
        val todayMonday = today.with(DayOfWeek.MONDAY)
        val newMonday = newDate.with(DayOfWeek.MONDAY)
        if (!newMonday.isAfter(todayMonday)) {
            selectWaterDate(if (newDate.isAfter(today)) today else newDate)
        }
    }
}
