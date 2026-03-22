package com.healthanalysis.app.presentation.screens.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val tips = listOf(
                    TipItem(
                        emoji = "\uD83E\uDDEA",
                        title = "\u0423\u0440\u043E\u0432\u0435\u043D\u044C \u0436\u0435\u043B\u0435\u0437\u0430 \u043F\u043E\u043D\u0438\u0436\u0435\u043D",
                        description = "\u0412\u0430\u0448 \u0443\u0440\u043E\u0432\u0435\u043D\u044C \u0436\u0435\u043B\u0435\u0437\u0430 (8.5 \u043C\u043A\u043C\u043E\u043B\u044C/\u043B) \u043D\u0438\u0436\u0435 \u043D\u043E\u0440\u043C\u044B. \u0420\u0435\u043A\u043E\u043C\u0435\u043D\u0434\u0443\u0435\u043C \u0443\u0432\u0435\u043B\u0438\u0447\u0438\u0442\u044C \u043F\u043E\u0442\u0440\u0435\u0431\u043B\u0435\u043D\u0438\u0435 \u043A\u0440\u0430\u0441\u043D\u043E\u0433\u043E \u043C\u044F\u0441\u0430, \u0448\u043F\u0438\u043D\u0430\u0442\u0430 \u0438 \u0431\u043E\u0431\u043E\u0432\u044B\u0445.",
                        priority = "high",
                        primaryAction = "\u041F\u043E\u0434\u0440\u043E\u0431\u043D\u0435\u0435",
                        secondaryAction = "\u041F\u0440\u043E\u0434\u0443\u043A\u0442\u044B"
                    ),
                    TipItem(
                        emoji = "\uD83D\uDCC8",
                        title = "\u0425\u043E\u043B\u0435\u0441\u0442\u0435\u0440\u0438\u043D \u043F\u043E\u0432\u044B\u0448\u0435\u043D",
                        description = "\u0423\u0440\u043E\u0432\u0435\u043D\u044C \u0445\u043E\u043B\u0435\u0441\u0442\u0435\u0440\u0438\u043D\u0430 (6.2 \u043C\u043C\u043E\u043B\u044C/\u043B) \u043F\u0440\u0435\u0432\u044B\u0448\u0430\u0435\u0442 \u043D\u043E\u0440\u043C\u0443. \u041E\u0433\u0440\u0430\u043D\u0438\u0447\u044C\u0442\u0435 \u0436\u0438\u0440\u043D\u0443\u044E \u043F\u0438\u0449\u0443 \u0438 \u0434\u043E\u0431\u0430\u0432\u044C\u0442\u0435 \u043E\u043C\u0435\u0433\u0430-3 \u0436\u0438\u0440\u043D\u044B\u0435 \u043A\u0438\u0441\u043B\u043E\u0442\u044B.",
                        priority = "high",
                        primaryAction = "\u041F\u043E\u0434\u0440\u043E\u0431\u043D\u0435\u0435",
                        secondaryAction = "\u0414\u0438\u0435\u0442\u0430"
                    ),
                    TipItem(
                        emoji = "\uD83D\uDCA7",
                        title = "\u041F\u0435\u0439\u0442\u0435 \u0431\u043E\u043B\u044C\u0448\u0435 \u0432\u043E\u0434\u044B",
                        description = "\u0412\u044B \u043F\u044C\u0451\u0442\u0435 \u0432 \u0441\u0440\u0435\u0434\u043D\u0435\u043C 6 \u0441\u0442\u0430\u043A\u0430\u043D\u043E\u0432 \u0432 \u0434\u0435\u043D\u044C. \u0420\u0435\u043A\u043E\u043C\u0435\u043D\u0434\u0443\u0435\u043C\u0430\u044F \u043D\u043E\u0440\u043C\u0430 \u2014 8 \u0441\u0442\u0430\u043A\u0430\u043D\u043E\u0432 \u0434\u043B\u044F \u043F\u043E\u0434\u0434\u0435\u0440\u0436\u0430\u043D\u0438\u044F \u0437\u0434\u043E\u0440\u043E\u0432\u044C\u044F.",
                        priority = "medium",
                        primaryAction = "\u041D\u0430\u043F\u043E\u043C\u043D\u0438\u0442\u044C"
                    ),
                    TipItem(
                        emoji = "\uD83C\uDFC3",
                        title = "\u0423\u0432\u0435\u043B\u0438\u0447\u044C\u0442\u0435 \u0430\u043A\u0442\u0438\u0432\u043D\u043E\u0441\u0442\u044C",
                        description = "\u0412\u0430\u0448\u0430 \u0441\u0440\u0435\u0434\u043D\u044F\u044F \u0430\u043A\u0442\u0438\u0432\u043D\u043E\u0441\u0442\u044C \u043D\u0438\u0436\u0435 \u0440\u0435\u043A\u043E\u043C\u0435\u043D\u0434\u0443\u0435\u043C\u043E\u0439. \u0414\u043E\u0431\u0430\u0432\u044C\u0442\u0435 30 \u043C\u0438\u043D\u0443\u0442 \u0445\u043E\u0434\u044C\u0431\u044B \u0435\u0436\u0435\u0434\u043D\u0435\u0432\u043D\u043E.",
                        priority = "medium",
                        primaryAction = "\u041F\u043B\u0430\u043D \u0442\u0440\u0435\u043D\u0438\u0440\u043E\u0432\u043E\u043A"
                    ),
                    TipItem(
                        emoji = "\u2705",
                        title = "\u0412\u0438\u0442\u0430\u043C\u0438\u043D D \u0432 \u043D\u043E\u0440\u043C\u0435",
                        description = "\u0412\u0430\u0448 \u0443\u0440\u043E\u0432\u0435\u043D\u044C \u0432\u0438\u0442\u0430\u043C\u0438\u043D\u0430 D (45 \u043D\u0433/\u043C\u043B) \u0432 \u043E\u043F\u0442\u0438\u043C\u0430\u043B\u044C\u043D\u043E\u043C \u0434\u0438\u0430\u043F\u0430\u0437\u043E\u043D\u0435. \u041F\u0440\u043E\u0434\u043E\u043B\u0436\u0430\u0439\u0442\u0435 \u043F\u0440\u0438\u043D\u0438\u043C\u0430\u0442\u044C \u0441\u043E\u043B\u043D\u0435\u0447\u043D\u044B\u0435 \u0432\u0430\u043D\u043D\u044B.",
                        priority = "medium",
                        primaryAction = "\u041E\u0442\u043B\u0438\u0447\u043D\u043E!"
                    )
                )

                _uiState.value = TipsUiState(
                    isLoading = false,
                    importantCount = tips.count { it.priority == "high" },
                    tipsCount = tips.size,
                    successCount = 1,
                    tips = tips
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
