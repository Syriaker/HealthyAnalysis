package com.healthanalysis.app.presentation.screens.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

@Composable
fun AnalysisScreen(viewModel: AnalysisViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> LoadingScreen()
            state.error != null -> ErrorScreen(
                message = state.error!!,
                onRetry = { viewModel.loadData() }
            )
            else -> AnalysisContent(state, viewModel)
        }

        if (!state.isLoading && state.error == null) {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = Primary,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }

        state.historyDialog?.let {
            HistoryDialog(dialog = it, onDismiss = { viewModel.closeHistory() })
        }

        state.addDialog?.let {
            AddAnalysisDialog(
                dialog = it,
                biomarkers = state.availableBiomarkers,
                onDismiss = { viewModel.closeAddDialog() },
                onDateChanged = { v -> viewModel.onAddDateChanged(v) },
                onLabChanged = { v -> viewModel.onAddLabChanged(v) },
                onCommentChanged = { v -> viewModel.onAddCommentChanged(v) },
                onValueChanged = { id, v -> viewModel.onAddValueChanged(id, v) },
                onSubmit = { viewModel.submitAddDialog() }
            )
        }
    }
}

@Composable
private fun AnalysisContent(state: AnalysisUiState, viewModel: AnalysisViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "Анализы крови",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            val subtitle = if (state.lastUpdate.isBlank())
                "У вас пока нет сохранённых анализов"
            else
                "Последнее обновление: ${state.lastUpdate}"
            Text(subtitle, fontSize = 14.sp, color = TextSecondary)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusCell(
                modifier = Modifier.weight(1f),
                bgColor = GreenBg,
                iconColor = GreenText,
                icon = "✓",
                count = state.normalCount,
                label = "В норме"
            )
            StatusCell(
                modifier = Modifier.weight(1f),
                bgColor = YellowBg,
                iconColor = YellowText,
                icon = "!",
                count = state.lowCount,
                label = "Ниже нормы"
            )
            StatusCell(
                modifier = Modifier.weight(1f),
                bgColor = RedBg,
                iconColor = RedText,
                icon = "!",
                count = state.highCount,
                label = "Выше нормы"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        val filters = listOf(
            "all" to "Все",
            "norm" to "Норма",
            "low" to "Низкие",
            "high" to "Высокие"
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { (key, label) ->
                val isActive = state.selectedFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .then(
                            if (isActive) Modifier
                                .shadow(2.dp, RoundedCornerShape(20.dp))
                                .background(Color.White)
                            else Modifier.background(Surface)
                        )
                        .clickable { viewModel.setFilter(key) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isActive) TextPrimary else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val filteredItems = when (state.selectedFilter) {
            "norm" -> state.items.filter { it.status == "norm" }
            "low" -> state.items.filter { it.status == "low" }
            "high" -> state.items.filter { it.status == "high" }
            else -> state.items
        }

        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Нет анализов для отображения. Нажмите «+», чтобы добавить.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            filteredItems.forEach { item ->
                AnalysisItemCard(item, onHistoryClick = { viewModel.openHistory(item.biomarkerId) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
private fun StatusCell(
    modifier: Modifier = Modifier,
    bgColor: Color,
    iconColor: Color,
    icon: String,
    count: Int,
    label: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = iconColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("$count", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(label, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun AnalysisItemCard(item: AnalysisItem, onHistoryClick: () -> Unit) {
    val (badgeBg, badgeText, badgeLabel) = when (item.status) {
        "norm" -> Triple(GreenBg, GreenText, "✓ Норма")
        "low" -> Triple(YellowBg, YellowTextDark, "! Ниже нормы")
        "high" -> Triple(RedBg, RedText, "! Выше нормы")
        else -> Triple(Surface, TextSecondary, "Нет данных")
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        badgeLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = badgeText
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            val normText = if (item.minNorm != null && item.maxNorm != null)
                "Норма: ${formatNumber(item.minNorm)}–${formatNumber(item.maxNorm)} ${item.unit}"
            else
                "Норма не задана"
            Text(normText, fontSize = 12.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    formatNumber(item.value),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    item.unit,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.history.size >= 2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                    ) {
                        MiniLineChart(
                            data = item.history.map { it.value.toFloat() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PurpleBg)
                        .clickable { onHistoryClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = "История",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniLineChart(data: List<Float>, modifier: Modifier = Modifier) {
    val primaryColor = Primary
    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas
        val width = size.width
        val height = size.height
        val maxVal = data.maxOrNull() ?: 1f
        val minVal = (data.minOrNull() ?: 0f) * 0.9f
        val range = (maxVal - minVal).coerceAtLeast(0.01f)
        val stepX = width / (data.size - 1)
        val points = data.mapIndexed { index, value ->
            Offset(
                x = index * stepX,
                y = height - ((value - minVal) / range * height)
            )
        }
        val path = Path()
        points.forEachIndexed { index, point ->
            if (index == 0) path.moveTo(point.x, point.y)
            else path.lineTo(point.x, point.y)
        }
        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
        points.forEach { point ->
            drawCircle(color = primaryColor, radius = 5f, center = point)
        }
        drawLine(
            color = primaryColor.copy(alpha = 0.2f),
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 1f
        )
    }
}

@Composable
private fun HistoryDialog(dialog: HistoryDialogState, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .clickable(enabled = false) { }
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            dialog.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        val normText = if (dialog.minNorm != null && dialog.maxNorm != null)
                            "Норма: ${formatNumber(dialog.minNorm)}–${formatNumber(dialog.maxNorm)} ${dialog.unit}"
                        else
                            "Норма не задана"
                        Text(normText, fontSize = 12.sp, color = TextSecondary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (dialog.points.isEmpty()) {
                    Text(
                        "История пока пуста. Добавьте новый результат.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                } else {
                    InteractiveHistoryChart(
                        points = dialog.points,
                        unit = dialog.unit,
                        minNorm = dialog.minNorm,
                        maxNorm = dialog.maxNorm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Все записи (${dialog.points.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .heightIn(max = 180.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        dialog.points.sortedByDescending { it.date }.forEach { p ->
                            val statusColor = when (p.status) {
                                "norm" -> GreenText
                                "low" -> YellowTextDark
                                "high" -> RedText
                                else -> TextSecondary
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    p.date.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru"))),
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    "${formatNumber(p.value)} ${dialog.unit}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = statusColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InteractiveHistoryChart(
    points: List<HistoryPoint>,
    unit: String,
    minNorm: Double?,
    maxNorm: Double?,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    var selectedIndex by remember(points) { mutableStateOf(points.lastIndex) }

    val values = points.map { it.value.toFloat() }
    val minData = values.minOrNull() ?: 0f
    val maxData = values.maxOrNull() ?: 0f
    val lowBound = minOf(minData, minNorm?.toFloat() ?: minData)
    val highBound = maxOf(maxData, maxNorm?.toFloat() ?: maxData)
    val padVal = (highBound - lowBound).coerceAtLeast(0.01f) * 0.1f
    val yMin = lowBound - padVal
    val yMax = highBound + padVal
    val range = (yMax - yMin).coerceAtLeast(0.01f)

    val primary = Primary
    val normBand = GreenBg
    val textSecondary = TextSecondary

    Column(modifier = modifier) {
        val selected = points[selectedIndex.coerceIn(0, points.lastIndex)]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    "${formatNumber(selected.value)} $unit",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    selected.date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            val statusBg = when (selected.status) {
                "norm" -> GreenBg
                "low" -> YellowBg
                "high" -> RedBg
                else -> Surface
            }
            val statusText = when (selected.status) {
                "norm" -> GreenText
                "low" -> YellowTextDark
                "high" -> RedText
                else -> TextSecondary
            }
            val statusLabel = when (selected.status) {
                "norm" -> "Норма"
                "low" -> "Низко"
                "high" -> "Высоко"
                else -> "Нет нормы"
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusBg)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(statusLabel, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = statusText)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(points) {
                    detectTapGestures { offset ->
                        val newIndex = indexFromTouch(offset.x, size.width.toFloat(), points.size)
                        selectedIndex = newIndex
                    }
                }
                .pointerInput(points) {
                    detectDragGestures { change, _ ->
                        val newIndex = indexFromTouch(change.position.x, size.width.toFloat(), points.size)
                        selectedIndex = newIndex
                    }
                }
        ) {
            val w = size.width
            val h = size.height
            val stepX = if (points.size == 1) 0f else w / (points.size - 1)

            if (minNorm != null && maxNorm != null) {
                val topY = h - ((maxNorm.toFloat() - yMin) / range * h)
                val botY = h - ((minNorm.toFloat() - yMin) / range * h)
                val yTop = minOf(topY, botY)
                val yBot = maxOf(topY, botY)
                drawRect(
                    color = normBand.copy(alpha = 0.55f),
                    topLeft = Offset(0f, yTop),
                    size = androidx.compose.ui.geometry.Size(w, (yBot - yTop).coerceAtLeast(1f))
                )
            }

            val offsets = values.mapIndexed { i, v ->
                val x = if (points.size == 1) w / 2f else i * stepX
                val y = h - ((v - yMin) / range * h)
                Offset(x, y)
            }
            val path = Path()
            offsets.forEachIndexed { i, o ->
                if (i == 0) path.moveTo(o.x, o.y) else path.lineTo(o.x, o.y)
            }
            drawPath(
                path = path,
                color = primary,
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
            offsets.forEachIndexed { i, o ->
                val isSelected = i == selectedIndex
                val r = if (isSelected) 10f else 6f
                drawCircle(color = primary, radius = r, center = o)
                if (isSelected) {
                    drawCircle(color = Color.White, radius = r - 4f, center = o)
                    drawLine(
                        color = primary.copy(alpha = 0.4f),
                        start = Offset(o.x, 0f),
                        end = Offset(o.x, h),
                        strokeWidth = 2f
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                points.first().date.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru"))),
                fontSize = 10.sp,
                color = textSecondary
            )
            if (points.size >= 3) {
                Text(
                    points[points.size / 2].date.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru"))),
                    fontSize = 10.sp,
                    color = textSecondary
                )
            }
            Text(
                points.last().date.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru"))),
                fontSize = 10.sp,
                color = textSecondary
            )
        }
    }
}

private fun indexFromTouch(x: Float, width: Float, count: Int): Int {
    if (count <= 1) return 0
    val stepX = width / (count - 1)
    val raw = (x / stepX).toInt()
    return raw.coerceIn(0, count - 1).let { i ->
        val left = abs(x - i * stepX)
        val right = if (i + 1 <= count - 1) abs(x - (i + 1) * stepX) else Float.MAX_VALUE
        if (right < left) i + 1 else i
    }
}

@Composable
private fun AddAnalysisDialog(
    dialog: AddDialogState,
    biomarkers: List<com.healthanalysis.app.data.models.PersonalNormResponse>,
    onDismiss: () -> Unit,
    onDateChanged: (String) -> Unit,
    onLabChanged: (String) -> Unit,
    onCommentChanged: (String) -> Unit,
    onValueChanged: (Int, String) -> Unit,
    onSubmit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .clickable(enabled = false) { }
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Новый анализ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    FieldLabel("Дата сдачи (ГГГГ-ММ-ДД)")
                    SimpleTextField(
                        value = dialog.date,
                        onValueChange = onDateChanged,
                        placeholder = "2025-10-01"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FieldLabel("Лаборатория")
                    SimpleTextField(
                        value = dialog.laboratory,
                        onValueChange = onLabChanged,
                        placeholder = "Название (необязательно)"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FieldLabel("Комментарий")
                    SimpleTextField(
                        value = dialog.comment,
                        onValueChange = onCommentChanged,
                        placeholder = "Необязательно"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Показатели",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (biomarkers.isEmpty()) {
                        Text(
                            "Нет доступных показателей. Заполните дату рождения в профиле.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    biomarkers.forEach { bio ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    bio.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary
                                )
                                Text(
                                    "${formatNumber(bio.minValue)}–${formatNumber(bio.maxValue)} ${bio.unit}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(modifier = Modifier.width(120.dp)) {
                                SimpleTextField(
                                    value = dialog.values[bio.id].orEmpty(),
                                    onValueChange = { raw ->
                                        val filtered = raw.filter { it.isDigit() || it == '.' || it == ',' }
                                        onValueChanged(bio.id, filtered)
                                    },
                                    placeholder = bio.unit,
                                    keyboardType = KeyboardType.Decimal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (dialog.error != null) {
                    Text(
                        dialog.error,
                        color = RedText,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (dialog.isSubmitting) Primary.copy(alpha = 0.6f) else Primary)
                        .clickable(enabled = !dialog.isSubmitting) { onSubmit() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (dialog.isSubmitting) "Сохранение…" else "Сохранить",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        if (value.isBlank() && placeholder.isNotBlank()) {
            Text(placeholder, fontSize = 14.sp, color = TextHint)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = TextPrimary,
                fontSize = 14.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) value.toInt().toString()
    else "%.2f".format(Locale.US, value).trimEnd('0').trimEnd('.')
}

private fun formatNumber(value: Float): String = formatNumber(value.toDouble())
