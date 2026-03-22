package com.healthanalysis.app.presentation.screens.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalysisItem(
    val name: String,
    val value: Double,
    val unit: String,
    val status: String, // "norm", "low", "high"
    val minNorm: Double,
    val maxNorm: Double,
    val history: List<Float> = emptyList()
)

data class AnalysisUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val normalCount: Int = 0,
    val lowCount: Int = 0,
    val highCount: Int = 0,
    val selectedFilter: String = "all",
    val lastUpdate: String = "",
    val items: List<AnalysisItem> = emptyList()
)

@HiltViewModel
class AnalysisViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Since analyses API is not yet implemented on backend,
                // we show demo data to demonstrate the UI
                val demoItems = listOf(
                    AnalysisItem(
                        name = "\u0413\u0435\u043C\u043E\u0433\u043B\u043E\u0431\u0438\u043D",
                        value = 142.0, unit = "\u0433/\u043B",
                        status = "norm", minNorm = 130.0, maxNorm = 170.0,
                        history = listOf(138f, 140f, 142f, 139f, 141f, 142f)
                    ),
                    AnalysisItem(
                        name = "\u0413\u043B\u044E\u043A\u043E\u0437\u0430",
                        value = 5.8, unit = "\u043C\u043C\u043E\u043B\u044C/\u043B",
                        status = "norm", minNorm = 3.9, maxNorm = 6.1,
                        history = listOf(5.2f, 5.5f, 5.7f, 5.6f, 5.8f, 5.8f)
                    ),
                    AnalysisItem(
                        name = "\u0425\u043E\u043B\u0435\u0441\u0442\u0435\u0440\u0438\u043D",
                        value = 6.2, unit = "\u043C\u043C\u043E\u043B\u044C/\u043B",
                        status = "high", minNorm = 3.0, maxNorm = 5.2,
                        history = listOf(5.0f, 5.3f, 5.6f, 5.8f, 6.0f, 6.2f)
                    ),
                    AnalysisItem(
                        name = "\u0416\u0435\u043B\u0435\u0437\u043E",
                        value = 8.5, unit = "\u043C\u043A\u043C\u043E\u043B\u044C/\u043B",
                        status = "low", minNorm = 10.7, maxNorm = 32.2,
                        history = listOf(12.0f, 11.0f, 10.5f, 9.8f, 9.0f, 8.5f)
                    ),
                    AnalysisItem(
                        name = "\u0412\u0438\u0442\u0430\u043C\u0438\u043D D",
                        value = 45.0, unit = "\u043D\u0433/\u043C\u043B",
                        status = "norm", minNorm = 30.0, maxNorm = 100.0,
                        history = listOf(28f, 32f, 36f, 40f, 43f, 45f)
                    ),
                    AnalysisItem(
                        name = "\u0422\u0421\u0413 (\u0442\u0438\u0440\u0435\u043E\u0442\u0440\u043E\u043F\u043D\u044B\u0439)",
                        value = 2.1, unit = "\u043C\u041C\u0415/\u043B",
                        status = "norm", minNorm = 0.4, maxNorm = 4.0,
                        history = listOf(1.8f, 1.9f, 2.0f, 2.0f, 2.1f, 2.1f)
                    )
                )

                val normalCount = demoItems.count { it.status == "norm" }
                val lowCount = demoItems.count { it.status == "low" }
                val highCount = demoItems.count { it.status == "high" }

                _uiState.value = AnalysisUiState(
                    isLoading = false,
                    normalCount = normalCount,
                    lowCount = lowCount,
                    highCount = highCount,
                    lastUpdate = "10 \u043C\u0430\u0440\u0442\u0430 2026",
                    items = demoItems
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }
}
