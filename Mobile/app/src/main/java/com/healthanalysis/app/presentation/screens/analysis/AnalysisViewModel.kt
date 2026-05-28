package com.healthanalysis.app.presentation.screens.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthanalysis.app.data.models.AnalysisRecordRequest
import com.healthanalysis.app.data.models.AnalysisRecordResponse
import com.healthanalysis.app.data.models.AnalysisResultRequest
import com.healthanalysis.app.data.models.LatestAnalysisResponse
import com.healthanalysis.app.data.models.PersonalNormResponse
import com.healthanalysis.app.data.repository.AnalysesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class HistoryPoint(
    val date: LocalDate,
    val value: Double,
    val status: String
)

data class AnalysisItem(
    val biomarkerId: Int,
    val name: String,
    val value: Double,
    val unit: String,
    val status: String, // "norm", "low", "high", "unknown"
    val minNorm: Double?,
    val maxNorm: Double?,
    val history: List<HistoryPoint> = emptyList()
)

data class HistoryDialogState(
    val biomarkerId: Int,
    val name: String,
    val unit: String,
    val minNorm: Double?,
    val maxNorm: Double?,
    val points: List<HistoryPoint>
)

data class AddDialogState(
    val date: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val laboratory: String = "",
    val comment: String = "",
    val values: Map<Int, String> = emptyMap(),
    val isSubmitting: Boolean = false,
    val error: String? = null
)

data class AnalysisUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val normalCount: Int = 0,
    val lowCount: Int = 0,
    val highCount: Int = 0,
    val selectedFilter: String = "all",
    val lastUpdate: String = "",
    val items: List<AnalysisItem> = emptyList(),
    val historyDialog: HistoryDialogState? = null,
    val addDialog: AddDialogState? = null,
    val availableBiomarkers: List<PersonalNormResponse> = emptyList()
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: AnalysesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState

    private var records: List<AnalysisRecordResponse> = emptyList()
    private var norms: List<PersonalNormResponse> = emptyList()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val normsResult = repository.getPersonalNorms()
            val recordsResult = repository.getRecords()
            val latestResult = repository.getLatest()

            val failure = listOf(normsResult, recordsResult, latestResult)
                .firstOrNull { it.isFailure }

            if (failure != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = failure.exceptionOrNull()?.message ?: "Ошибка загрузки"
                )
                return@launch
            }

            norms = normsResult.getOrNull().orEmpty()
            records = recordsResult.getOrNull().orEmpty()
            val latest = latestResult.getOrNull().orEmpty()

            rebuildState(latest)
        }
    }

    private fun computeStatus(value: Double, minNorm: Double?, maxNorm: Double?): String {
        if (minNorm == null || maxNorm == null) return "unknown"
        return when {
            value < minNorm -> "low"
            value > maxNorm -> "high"
            else -> "norm"
        }
    }

    private fun rebuildState(latest: List<LatestAnalysisResponse>) {
        val normsById = norms.associateBy { it.id }
        val historyByBiomarker: Map<Int, List<HistoryPoint>> = buildHistoryMap(normsById)

        val items = latest.map { l ->
            val norm = normsById[l.biomarkerId]
            AnalysisItem(
                biomarkerId = l.biomarkerId,
                name = l.biomarkerName,
                value = l.value,
                unit = l.unit,
                status = computeStatus(l.value, norm?.minValue, norm?.maxValue),
                minNorm = norm?.minValue,
                maxNorm = norm?.maxValue,
                history = historyByBiomarker[l.biomarkerId].orEmpty()
            )
        }.sortedBy { it.name }

        val normalCount = items.count { it.status == "norm" }
        val lowCount = items.count { it.status == "low" }
        val highCount = items.count { it.status == "high" }

        val latestDate = latest.maxOfOrNull { it.date } ?: ""
        val formattedDate = formatDate(latestDate)

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = null,
            normalCount = normalCount,
            lowCount = lowCount,
            highCount = highCount,
            lastUpdate = formattedDate,
            items = items,
            availableBiomarkers = norms.sortedBy { it.name }
        )
    }

    private fun buildHistoryMap(normsById: Map<Int, PersonalNormResponse>): Map<Int, List<HistoryPoint>> {
        val raw = mutableMapOf<Int, MutableList<HistoryPoint>>()
        records.forEach { rec ->
            val date = parseDate(rec.date) ?: return@forEach
            rec.results.forEach { res ->
                val list = raw.getOrPut(res.biomarker) { mutableListOf() }
                val norm = normsById[res.biomarker]
                list += HistoryPoint(
                    date = date,
                    value = res.value,
                    status = computeStatus(res.value, norm?.minValue, norm?.maxValue)
                )
            }
        }
        return raw.mapValues { (_, list) -> list.sortedBy { it.date } }
    }

    private fun parseDate(s: String?): LocalDate? {
        if (s.isNullOrBlank()) return null
        return try {
            LocalDate.parse(s)
        } catch (e: Exception) {
            null
        }
    }

    private fun formatDate(iso: String): String {
        val date = parseDate(iso) ?: return ""
        return date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru")))
    }

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun openHistory(biomarkerId: Int) {
        val item = _uiState.value.items.firstOrNull { it.biomarkerId == biomarkerId } ?: return
        _uiState.value = _uiState.value.copy(
            historyDialog = HistoryDialogState(
                biomarkerId = item.biomarkerId,
                name = item.name,
                unit = item.unit,
                minNorm = item.minNorm,
                maxNorm = item.maxNorm,
                points = item.history
            )
        )
    }

    fun closeHistory() {
        _uiState.value = _uiState.value.copy(historyDialog = null)
    }

    fun openAddDialog() {
        _uiState.value = _uiState.value.copy(addDialog = AddDialogState())
    }

    fun closeAddDialog() {
        _uiState.value = _uiState.value.copy(addDialog = null)
    }

    fun onAddDateChanged(value: String) {
        val dialog = _uiState.value.addDialog ?: return
        _uiState.value = _uiState.value.copy(
            addDialog = dialog.copy(date = value, error = null)
        )
    }

    fun onAddLabChanged(value: String) {
        val dialog = _uiState.value.addDialog ?: return
        _uiState.value = _uiState.value.copy(
            addDialog = dialog.copy(laboratory = value)
        )
    }

    fun onAddCommentChanged(value: String) {
        val dialog = _uiState.value.addDialog ?: return
        _uiState.value = _uiState.value.copy(
            addDialog = dialog.copy(comment = value)
        )
    }

    fun onAddValueChanged(biomarkerId: Int, value: String) {
        val dialog = _uiState.value.addDialog ?: return
        val newValues = dialog.values.toMutableMap()
        if (value.isBlank()) newValues.remove(biomarkerId) else newValues[biomarkerId] = value
        _uiState.value = _uiState.value.copy(addDialog = dialog.copy(values = newValues))
    }

    fun submitAddDialog() {
        val dialog = _uiState.value.addDialog ?: return

        val date = try {
            LocalDate.parse(dialog.date)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                addDialog = dialog.copy(error = "Неверный формат даты. Используйте ГГГГ-ММ-ДД.")
            )
            return
        }

        if (date.isAfter(LocalDate.now())) {
            _uiState.value = _uiState.value.copy(
                addDialog = dialog.copy(error = "Нельзя ввести анализ на дату, которая ещё не наступила.")
            )
            return
        }

        val results = dialog.values.mapNotNull { (id, raw) ->
            val parsed = raw.replace(',', '.').toDoubleOrNull() ?: return@mapNotNull null
            AnalysisResultRequest(biomarker = id, value = parsed)
        }

        if (results.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                addDialog = dialog.copy(error = "Заполните значение хотя бы одного показателя.")
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                addDialog = dialog.copy(isSubmitting = true, error = null)
            )
            val request = AnalysisRecordRequest(
                date = dialog.date,
                laboratory = dialog.laboratory,
                comment = dialog.comment,
                results = results
            )
            repository.createRecord(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(addDialog = null)
                    loadData()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        addDialog = dialog.copy(
                            isSubmitting = false,
                            error = e.message ?: "Ошибка сохранения"
                        )
                    )
                }
        }
    }
}
