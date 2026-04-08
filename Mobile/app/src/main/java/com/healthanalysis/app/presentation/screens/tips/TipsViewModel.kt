package com.healthanalysis.app.presentation.screens.tips

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class TipItem(
    val emoji: String,
    val title: String,
    val description: String,
    val priority: String, // "high", "medium"
    val primaryAction: String,
    val secondaryAction: String? = null
)

data class TipsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val importantCount: Int = 0,
    val tipsCount: Int = 0,
    val successCount: Int = 0,
    val tips: List<TipItem> = emptyList()
)

@HiltViewModel
class TipsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TipsUiState())
    val uiState: StateFlow<TipsUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        // No backend API for tips yet — show empty state
        _uiState.value = TipsUiState(isLoading = false)
    }
}
