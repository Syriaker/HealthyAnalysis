package com.healthanalysis.app.presentation.screens.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TipItem(
    val emoji: String,
    val title: String,
    val description: String,
    val priority: String, // "high", "medium"
    val primaryAction: String,
    val secondaryAction: String? = null
)

data class AiHistoryEntry(val date: String, val text: String)

data class TipsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val tips: List<TipItem> = emptyList(),
    val aiAdvice: String? = null,
    val aiDate: String? = null,
    val aiHistory: List<AiHistoryEntry> = emptyList(),
    val isAnalyzing: Boolean = false,
    val analyzeError: String? = null
)

@HiltViewModel
class TipsViewModel @Inject constructor(
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TipsUiState())
    val uiState: StateFlow<TipsUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            aiRepository.getHistory().onSuccess { history ->
                val entries = history.mapNotNull { item ->
                    val text = item.text ?: return@mapNotNull null
                    AiHistoryEntry(date = item.date ?: "", text = text)
                }
                val latest = entries.firstOrNull()
                _uiState.value = _uiState.value.copy(
                    aiAdvice = latest?.text,
                    aiDate = latest?.date?.takeIf { it.isNotEmpty() },
                    aiHistory = entries
                )
            }
        }
    }

    fun analyzeMetrics() {
        if (_uiState.value.isAnalyzing) return
        _uiState.value = _uiState.value.copy(isAnalyzing = true, analyzeError = null)

        viewModelScope.launch {
            aiRepository.generateAdvice()
                .onSuccess { generateResponse ->
                    pollStatus(generateResponse.taskId)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        analyzeError = e.message ?: "Ошибка запуска анализа"
                    )
                }
        }
    }

    private suspend fun pollStatus(taskId: Int) {
        while (true) {
            delay(3000)
            aiRepository.checkStatus(taskId)
                .onSuccess { statusResponse ->
                    if (statusResponse.status == "ready") {
                        _uiState.value = _uiState.value.copy(
                            isAnalyzing = false,
                            aiAdvice = statusResponse.adviceText,
                            aiDate = null
                        )
                        refreshHistory()
                        return
                    }
                    // status == "processing" — продолжаем polling
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        analyzeError = e.message ?: "Ошибка проверки статуса"
                    )
                    return
                }
        }
    }

    private fun refreshHistory() {
        viewModelScope.launch {
            aiRepository.getHistory().onSuccess { history ->
                val entries = history.mapNotNull { item ->
                    val text = item.text ?: return@mapNotNull null
                    AiHistoryEntry(date = item.date ?: "", text = text)
                }
                _uiState.value = _uiState.value.copy(aiHistory = entries)
            }
        }
    }
}
