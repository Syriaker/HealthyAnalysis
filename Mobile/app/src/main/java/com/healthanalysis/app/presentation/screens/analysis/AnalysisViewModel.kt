package com.healthanalysis.app.presentation.screens.analysis

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        // Backend analyses API is not yet implemented — show empty state
        _uiState.value = AnalysisUiState(isLoading = false)
    }

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }
}
