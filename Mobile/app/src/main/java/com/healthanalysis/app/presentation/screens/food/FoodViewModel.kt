package com.healthanalysis.app.presentation.screens.food

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class DisplayFoodItem(
    val name: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)

data class FoodUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val caloriesConsumed: Int = 0,
    val proteins: Int = 0,
    val fats: Int = 0,
    val carbs: Int = 0,
    val breakfastItems: List<DisplayFoodItem> = emptyList(),
    val lunchItems: List<DisplayFoodItem> = emptyList(),
    val dinnerItems: List<DisplayFoodItem> = emptyList(),
    val snackItems: List<DisplayFoodItem> = emptyList(),
    val breakfastCalories: Int = 0,
    val lunchCalories: Int = 0,
    val dinnerCalories: Int = 0,
    val snackCalories: Int = 0,
    val showAddDialog: Boolean = false
)

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

    fun addManualEntry(
        name: String,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int,
        mealType: String
    ) {
        val entry = DisplayFoodItem(name, calories, proteins, fats, carbs)
        val current = _uiState.value

        val newState = when (mealType) {
            "breakfast" -> current.copy(
                breakfastItems = current.breakfastItems + entry,
                breakfastCalories = current.breakfastCalories + calories
            )
            "lunch" -> current.copy(
                lunchItems = current.lunchItems + entry,
                lunchCalories = current.lunchCalories + calories
            )
            "dinner" -> current.copy(
                dinnerItems = current.dinnerItems + entry,
                dinnerCalories = current.dinnerCalories + calories
            )
            else -> current.copy(
                snackItems = current.snackItems + entry,
                snackCalories = current.snackCalories + calories
            )
        }

        val allItems = newState.breakfastItems + newState.lunchItems + newState.dinnerItems + newState.snackItems
        _uiState.value = newState.copy(
            caloriesConsumed = newState.breakfastCalories + newState.lunchCalories + newState.dinnerCalories + newState.snackCalories,
            proteins = allItems.sumOf { it.proteins },
            fats = allItems.sumOf { it.fats },
            carbs = allItems.sumOf { it.carbs },
            showAddDialog = false
        )
    }
}
