package com.healthanalysis.app.presentation.screens.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*

@Composable
fun AnalysisScreen(viewModel: AnalysisViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadData() }
        )
        else -> AnalysisContent(state, viewModel)
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
        // Header
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "\u0410\u043D\u0430\u043B\u0438\u0437\u044B \u043A\u0440\u043E\u0432\u0438",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "\u041F\u043E\u0441\u043B\u0435\u0434\u043D\u0435\u0435 \u043E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u0435: ${state.lastUpdate}",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        // Status Grid
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
                icon = "\u2713",
                count = state.normalCount,
                label = "\u0412 \u043D\u043E\u0440\u043C\u0435"
            )
            StatusCell(
                modifier = Modifier.weight(1f),
                bgColor = YellowBg,
                iconColor = YellowText,
                icon = "!",
                count = state.lowCount,
                label = "\u041D\u0438\u0436\u0435 \u043D\u043E\u0440\u043C\u044B"
            )
            StatusCell(
                modifier = Modifier.weight(1f),
                bgColor = RedBg,
                iconColor = RedText,
                icon = "!",
                count = state.highCount,
                label = "\u0412\u044B\u0448\u0435 \u043D\u043E\u0440\u043C\u044B"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Filter Tabs
        val filters = listOf(
            "all" to "\u0412\u0441\u0435",
            "norm" to "\u041D\u043E\u0440\u043C\u0430",
            "low" to "\u041D\u0438\u0437\u043A\u0438\u0435",
            "high" to "\u0412\u044B\u0441\u043E\u043A\u0438\u0435"
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

        // Analysis Items
        val filteredItems = when (state.selectedFilter) {
            "norm" -> state.items.filter { it.status == "norm" }
            "low" -> state.items.filter { it.status == "low" }
            "high" -> state.items.filter { it.status == "high" }
            else -> state.items
        }

        filteredItems.forEach { item ->
            AnalysisItemCard(item)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
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
                Text(
                    icon,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "$count",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(label, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun AnalysisItemCard(item: AnalysisItem) {
    val (badgeBg, badgeText, badgeLabel) = when (item.status) {
        "norm" -> Triple(GreenBg, GreenText, "\u2713 \u041D\u043E\u0440\u043C\u0430")
        "low" -> Triple(YellowBg, YellowTextDark, "! \u041D\u0438\u0436\u0435 \u043D\u043E\u0440\u043C\u044B")
        "high" -> Triple(RedBg, RedText, "! \u0412\u044B\u0448\u0435 \u043D\u043E\u0440\u043C\u044B")
        else -> Triple(Surface, TextSecondary, "\u041D\u0435\u0442 \u0434\u0430\u043D\u043D\u044B\u0445")
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
            Text(
                "\u041D\u043E\u0440\u043C\u0430: ${item.minNorm}-${item.maxNorm} ${item.unit}",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "${item.value}",
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

            // Mini chart
            if (item.history.isNotEmpty()) {
                MiniLineChart(
                    data = item.history,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    }
}

@Composable
private fun MiniLineChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
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
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )

        points.forEach { point ->
            drawCircle(color = primaryColor, radius = 3f, center = point)
        }

        // Bottom line
        drawLine(
            color = primaryColor.copy(alpha = 0.2f),
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 1f
        )
    }
}
