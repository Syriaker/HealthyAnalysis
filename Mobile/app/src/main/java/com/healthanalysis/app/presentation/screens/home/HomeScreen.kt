package com.healthanalysis.app.presentation.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LifecycleResumeEffect(Unit) {
        viewModel.loadData()
        onPauseOrDispose {}
    }

    when {
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadData() }
        )
        else -> HomeContent(
            state = state,
            onAddWater = viewModel::addWater,
            onShowWaterHistory = viewModel::showWaterHistory,
            onHideWaterHistory = viewModel::hideWaterHistory,
            onSelectWaterDate = viewModel::selectWaterDate,
            onPreviousWaterWeek = viewModel::previousWaterWeek,
            onNextWaterWeek = viewModel::nextWaterWeek
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onAddWater: (Int) -> Unit,
    onShowWaterHistory: () -> Unit,
    onHideWaterHistory: () -> Unit,
    onSelectWaterDate: (LocalDate) -> Unit,
    onPreviousWaterWeek: () -> Unit,
    onNextWaterWeek: () -> Unit
) {
    var customWaterAmount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Добро пожаловать! 👋",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = LocalDate.now().format(
                    DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
                ),
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = PrimaryDark.copy(alpha = 0.3f),
                    spotColor = PrimaryDark.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Primary, PrimaryDark),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Калории сегодня",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${state.caloriesConsumed} ккал",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚡", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WaterDrop,
                                contentDescription = null,
                                tint = BlueText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Вода", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                "${state.waterMl} мл",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                    TextButton(onClick = onShowWaterHistory) {
                        Text("История →", fontSize = 12.sp, color = Primary)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onAddWater(-250) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("−250 мл", fontSize = 13.sp)
                    }
                    Button(
                        onClick = { onAddWater(250) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                    ) {
                        Text("+250 мл", fontSize = 13.sp, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val customAmt = customWaterAmount.toIntOrNull() ?: 0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customWaterAmount,
                        onValueChange = { customWaterAmount = it.filter { c -> c.isDigit() } },
                        placeholder = { Text("Свой объём, мл", color = TextHint, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedButton(
                        onClick = { if (customAmt > 0) { onAddWater(-customAmt); customWaterAmount = "" } },
                        enabled = customAmt > 0,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("−", fontSize = 18.sp)
                    }
                    Button(
                        onClick = { if (customAmt > 0) { onAddWater(customAmt); customWaterAmount = "" } },
                        enabled = customAmt > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("+", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Калории за неделю",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Нажмите или проведите по графику, чтобы увидеть значение",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                WeeklyCaloriesChart(
                    data = state.weeklyCalories,
                    days = state.weekDays
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (state.showWaterHistory) {
        WaterHistoryDialog(
            state = state,
            onDismiss = onHideWaterHistory,
            onSelectDay = onSelectWaterDate,
            onPreviousWeek = onPreviousWaterWeek,
            onNextWeek = onNextWaterWeek,
            onAddWater = onAddWater
        )
    }
}

@Composable
private fun WaterHistoryDialog(
    state: HomeUiState,
    onDismiss: () -> Unit,
    onSelectDay: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onAddWater: (Int) -> Unit
) {
    val today = LocalDate.now()
    val selected = state.selectedWaterDate
    val monday = selected.with(DayOfWeek.MONDAY)
    val todayMonday = today.with(DayOfWeek.MONDAY)
    val canGoNext = monday.isBefore(todayMonday)

    var dialogCustomAmount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "История воды",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = null, tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onPreviousWeek) {
                        Text("←", fontSize = 20.sp, color = Primary)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (0..6).forEach { i ->
                            val date = monday.plusDays(i.toLong())
                            val isFuture = date.isAfter(today)
                            val isSelected = date == selected
                            val dayLabel = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")[i]

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Primary else Color.Transparent)
                                    .then(
                                        if (!isFuture) Modifier.clickable { onSelectDay(date) }
                                        else Modifier
                                    )
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    dayLabel,
                                    fontSize = 10.sp,
                                    color = when {
                                        isSelected -> Color.White
                                        isFuture -> TextHint
                                        else -> TextSecondary
                                    }
                                )
                                Text(
                                    date.dayOfMonth.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = when {
                                        isSelected -> Color.White
                                        isFuture -> TextHint
                                        else -> TextPrimary
                                    }
                                )
                            }
                        }
                    }

                    TextButton(onClick = onNextWeek, enabled = canGoNext) {
                        Text("→", fontSize = 20.sp, color = if (canGoNext) Primary else TextHint)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = selected.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))),
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${state.selectedWaterMl} мл",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onAddWater(-250) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("−250 мл")
                    }
                    Button(
                        onClick = { onAddWater(250) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                    ) {
                        Text("+250 мл", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val dialogAmt = dialogCustomAmount.toIntOrNull() ?: 0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = dialogCustomAmount,
                        onValueChange = { dialogCustomAmount = it.filter { c -> c.isDigit() } },
                        placeholder = { Text("Свой объём, мл", color = TextHint, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedButton(
                        onClick = { if (dialogAmt > 0) { onAddWater(-dialogAmt); dialogCustomAmount = "" } },
                        enabled = dialogAmt > 0,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("−", fontSize = 18.sp)
                    }
                    Button(
                        onClick = { if (dialogAmt > 0) { onAddWater(dialogAmt); dialogCustomAmount = "" } },
                        enabled = dialogAmt > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("+", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyCaloriesChart(
    data: List<Float>,
    days: List<String>
) {
    if (data.isEmpty()) return

    var selectedIndex by remember(data) { mutableStateOf(data.lastIndex.coerceAtLeast(0)) }
    val primary = Primary
    val accent = PrimaryDark

    Column(modifier = Modifier.fillMaxSize()) {
        val selected = selectedIndex.coerceIn(0, data.lastIndex)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    days.getOrNull(selected) ?: "",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    "${data[selected].toInt()} ккал",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
            val avg = if (data.isNotEmpty()) data.sum() / data.size else 0f
            Text(
                "Среднее: ${avg.toInt()}",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(data) {
                    detectTapGestures { offset ->
                        selectedIndex = indexFromX(offset.x, size.width.toFloat(), data.size)
                    }
                }
                .pointerInput(data) {
                    detectDragGestures { change, _ ->
                        selectedIndex = indexFromX(change.position.x, size.width.toFloat(), data.size)
                    }
                }
        ) {
            val width = size.width
            val height = size.height
            val maxValue = (data.maxOrNull() ?: 1f).coerceAtLeast(1f)
            val range = maxValue.coerceAtLeast(1f)

            val stepX = if (data.size <= 1) 0f else width / (data.size - 1)

            val points = data.mapIndexed { index, value ->
                val x = if (data.size == 1) width / 2f else index * stepX
                Offset(x = x, y = height - (value / range * height))
            }

            val fillPath = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points[0].x, height)
                    lineTo(points[0].x, points[0].y)
                    for (i in 1 until points.size) lineTo(points[i].x, points[i].y)
                    lineTo(points.last().x, height)
                    close()
                }
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(primary.copy(alpha = 0.25f), primary.copy(alpha = 0.0f))
                )
            )

            val linePath = Path()
            points.forEachIndexed { index, point ->
                if (index == 0) linePath.moveTo(point.x, point.y)
                else linePath.lineTo(point.x, point.y)
            }
            drawPath(
                path = linePath,
                color = primary,
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )

            points.forEachIndexed { index, point ->
                val isSel = index == selected
                val r = if (isSel) 10f else 6f
                drawCircle(color = primary, radius = r, center = point)
                if (isSel) {
                    drawCircle(color = Color.White, radius = r - 4f, center = point)
                    drawLine(
                        color = accent.copy(alpha = 0.4f),
                        start = Offset(point.x, 0f),
                        end = Offset(point.x, height),
                        strokeWidth = 2f
                    )
                } else {
                    drawCircle(color = Color.White, radius = 2f, center = point)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEachIndexed { index, day ->
                val isSel = index == selected
                Text(
                    day,
                    fontSize = 11.sp,
                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSel) Primary else TextSecondary
                )
            }
        }
    }
}

private fun indexFromX(x: Float, width: Float, count: Int): Int {
    if (count <= 1) return 0
    val stepX = width / (count - 1)
    val raw = (x / stepX).toInt().coerceIn(0, count - 1)
    val left = abs(x - raw * stepX)
    val right = if (raw + 1 <= count - 1) abs(x - (raw + 1) * stepX) else Float.MAX_VALUE
    return if (right < left) raw + 1 else raw
}
