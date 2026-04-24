package com.healthanalysis.app.presentation.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.models.CustomProductRequest
import com.healthanalysis.app.data.models.FoodLogRequest
import com.healthanalysis.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    val showAddDialog: Boolean = false,
    val isAddingFood: Boolean = false,
    val addError: String? = null
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
class FoodViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState

    init {
        loadData(showFullLoader = true)
    }

    // Вызывается при возврате на экран (lifecycle ON_RESUME) и при первом запуске.
    fun loadData(showFullLoader: Boolean = false) {
        val dateKey = _uiState.value.selectedDate.toString()
        viewModelScope.launch {
            if (showFullLoader) {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            nutritionRepository.getFoodLogs(dateKey)
                .onSuccess { logs ->
                    val breakfast = mutableListOf<DisplayFoodItem>()
                    val lunch = mutableListOf<DisplayFoodItem>()
                    val dinner = mutableListOf<DisplayFoodItem>()
                    val snack = mutableListOf<DisplayFoodItem>()

                    logs.forEach { log ->
                        val multiplier = log.weight / 100.0
                        val item = DisplayFoodItem(
                            name = log.product.name,
                            calories = (log.product.calories * multiplier).toInt(),
                            proteins = (log.product.proteins * multiplier).toInt(),
                            fats = (log.product.fats * multiplier).toInt(),
                            carbs = (log.product.carbs * multiplier).toInt()
                        )
                        when (log.mealType) {
                            "breakfast" -> breakfast.add(item)
                            "lunch" -> lunch.add(item)
                            "dinner" -> dinner.add(item)
                            else -> snack.add(item)
                        }
                    }

                    val dayData = DayFoodData(breakfast, lunch, dinner, snack)
                    val current = _uiState.value
                    _uiState.value = current.copy(
                        isLoading = false,
                        error = null,
                        foodByDate = current.foodByDate + (dateKey to dayData)
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка загрузки данных"
                    )
                }
        }
    }

    // Загружает данные только если для этой даты ещё нет кэша.
    private fun loadIfNotCached() {
        val dateKey = _uiState.value.selectedDate.toString()
        if (!_uiState.value.foodByDate.containsKey(dateKey)) {
            loadData()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true, addError = null)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false, addError = null, isAddingFood = false)
    }

    fun selectDay(date: LocalDate) {
        if (!date.isAfter(LocalDate.now())) {
            _uiState.value = _uiState.value.copy(selectedDate = date)
            loadIfNotCached()
        }
    }

    fun previousWeek() {
        _uiState.value = _uiState.value.copy(
            selectedDate = _uiState.value.selectedDate.minusWeeks(1)
        )
        loadIfNotCached()
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
            loadIfNotCached()
        }
    }

    // Шаг 1: POST /nutrition/products/custom/ → получаем product_id.
    // Шаг 2: POST /nutrition/log/ с product_id и weight=100
    //   (вес 100 г означает, что nutrition-значения совпадают с введёнными пользователем).
    // Продукт добавляется на экран только после успешного ответа 201 от сервера.
    fun addManualEntry(
        name: String,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int,
        mealType: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAddingFood = true, addError = null)

            val productResult = nutritionRepository.createCustomProduct(
                CustomProductRequest(
                    name = name,
                    calories = calories.toDouble(),
                    proteins = proteins.toDouble(),
                    fats = fats.toDouble(),
                    carbs = carbs.toDouble()
                )
            )

            if (productResult.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isAddingFood = false,
                    addError = productResult.exceptionOrNull()?.message ?: "Не удалось создать продукт"
                )
                return@launch
            }

            val productId = productResult.getOrNull()?.id ?: run {
                _uiState.value = _uiState.value.copy(
                    isAddingFood = false,
                    addError = "Сервер не вернул ID продукта"
                )
                return@launch
            }

            val logResult = nutritionRepository.logFood(
                FoodLogRequest(productId = productId, weight = 100, mealType = mealType)
            )

            logResult.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isAddingFood = false,
                    addError = null,
                    showAddDialog = false
                )
                loadData()
            }

            logResult.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isAddingFood = false,
                    addError = e.message ?: "Не удалось добавить в дневник"
                )
            }
        }
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
