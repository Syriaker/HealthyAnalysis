package com.healthanalysis.app.presentation.screens.food

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

data class DisplayFoodItem(
    val name: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)

data class DayFoodData(
    val breakfastItems: List<DisplayFoodItem> = emptyList(),
    val lunchItems: List<DisplayFoodItem> = emptyList(),
    val dinnerItems: List<DisplayFoodItem> = emptyList(),
    val snackItems: List<DisplayFoodItem> = emptyList()
)

data class FoodUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val foodByDate: Map<String, DayFoodData> = emptyMap(),
    val showAddDialog: Boolean = false
) {
    private val selectedDay: DayFoodData get() = foodByDate[selectedDate.toString()] ?: DayFoodData()

    val breakfastItems: List<DisplayFoodItem> get() = selectedDay.breakfastItems
    val lunchItems: List<DisplayFoodItem> get() = selectedDay.lunchItems
    val dinnerItems: List<DisplayFoodItem> get() = selectedDay.dinnerItems
    val snackItems: List<DisplayFoodItem> get() = selectedDay.snackItems

    val breakfastCalories: Int get() = breakfastItems.sumOf { it.calories }
    val lunchCalories: Int get() = lunchItems.sumOf { it.calories }
    val dinnerCalories: Int get() = dinnerItems.sumOf { it.calories }
    val snackCalories: Int get() = snackItems.sumOf { it.calories }

    val caloriesConsumed: Int get() = breakfastCalories + lunchCalories + dinnerCalories + snackCalories
    val proteins: Int get() = (breakfastItems + lunchItems + dinnerItems + snackItems).sumOf { it.proteins }
    val fats: Int get() = (breakfastItems + lunchItems + dinnerItems + snackItems).sumOf { it.fats }
    val carbs: Int get() = (breakfastItems + lunchItems + dinnerItems + snackItems).sumOf { it.carbs }
}

@HiltViewModel
class FoodViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun selectDay(date: LocalDate) {
        if (!date.isAfter(LocalDate.now())) {
            _uiState.value = _uiState.value.copy(selectedDate = date)
        }
    }

    fun previousWeek() {
        _uiState.value = _uiState.value.copy(
            selectedDate = _uiState.value.selectedDate.minusWeeks(1)
        )
    }

    fun nextWeek() {
        val current = _uiState.value
        val today = LocalDate.now()
        val newDate = current.selectedDate.plusWeeks(1)
        val todayMonday = today.with(DayOfWeek.MONDAY)
        val newMonday = newDate.with(DayOfWeek.MONDAY)
        if (!newMonday.isAfter(todayMonday)) {
            _uiState.value = current.copy(
                selectedDate = if (newDate.isAfter(today)) today else newDate
            )
        }
    }

    fun addManualEntry(
        name: String,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int,
        mealType: String
    ) {
        val current = _uiState.value
        val dateKey = current.selectedDate.toString()
        val currentDay = current.foodByDate[dateKey] ?: DayFoodData()
        val entry = DisplayFoodItem(name, calories, proteins, fats, carbs)

        val updatedDay = when (mealType) {
            "breakfast" -> currentDay.copy(breakfastItems = currentDay.breakfastItems + entry)
            "lunch" -> currentDay.copy(lunchItems = currentDay.lunchItems + entry)
            "dinner" -> currentDay.copy(dinnerItems = currentDay.dinnerItems + entry)
            else -> currentDay.copy(snackItems = currentDay.snackItems + entry)
        }

        _uiState.value = current.copy(
            foodByDate = current.foodByDate + (dateKey to updatedDay),
            showAddDialog = false
        )
    }

    fun removeEntry(mealType: String, index: Int) {
        val current = _uiState.value
        val dateKey = current.selectedDate.toString()
        val currentDay = current.foodByDate[dateKey] ?: return

        val updatedDay = when (mealType) {
            "breakfast" -> currentDay.copy(
                breakfastItems = currentDay.breakfastItems.filterIndexed { i, _ -> i != index }
            )
            "lunch" -> currentDay.copy(
                lunchItems = currentDay.lunchItems.filterIndexed { i, _ -> i != index }
            )
            "dinner" -> currentDay.copy(
                dinnerItems = currentDay.dinnerItems.filterIndexed { i, _ -> i != index }
            )
            else -> currentDay.copy(
                snackItems = currentDay.snackItems.filterIndexed { i, _ -> i != index }
            )
        }

        _uiState.value = current.copy(
            foodByDate = current.foodByDate + (dateKey to updatedDay)
        )
    }
}
