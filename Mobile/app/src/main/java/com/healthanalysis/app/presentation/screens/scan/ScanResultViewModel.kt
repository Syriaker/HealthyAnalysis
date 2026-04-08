package com.healthanalysis.app.presentation.screens.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.models.FoodLogRequest
import com.healthanalysis.app.data.models.ProductResponse
import com.healthanalysis.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScanResultUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val product: ProductResponse? = null,
    val weight: String = "100",
    val selectedMealType: String = "snack",
    val isLogging: Boolean = false,
    val logSuccess: Boolean = false
)

@HiltViewModel
class ScanResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val barcode: String = savedStateHandle["barcode"] ?: ""

    private val _uiState = MutableStateFlow(ScanResultUiState())
    val uiState: StateFlow<ScanResultUiState> = _uiState

    init {
        if (barcode.isNotEmpty()) {
            scanProduct()
        } else {
            _uiState.value = ScanResultUiState(isLoading = false, error = "Штрих-код пуст")
        }
    }

    private fun scanProduct() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            nutritionRepository.scanProduct(barcode)
                .onSuccess { product ->
                    _uiState.value = _uiState.value.copy(isLoading = false, product = product)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Продукт не найден"
                    )
                }
        }
    }

    fun onWeightChanged(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun onMealTypeChanged(mealType: String) {
        _uiState.value = _uiState.value.copy(selectedMealType = mealType)
    }

    fun logFood() {
        val product = _uiState.value.product ?: return
        val weight = _uiState.value.weight.toIntOrNull() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLogging = true)
            nutritionRepository.logFood(
                FoodLogRequest(
                    productId = product.id,
                    weight = weight,
                    mealType = _uiState.value.selectedMealType
                )
            ).onSuccess {
                _uiState.value = _uiState.value.copy(isLogging = false, logSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLogging = false,
                    error = e.message ?: "Ошибка добавления"
                )
            }
        }
    }
}
