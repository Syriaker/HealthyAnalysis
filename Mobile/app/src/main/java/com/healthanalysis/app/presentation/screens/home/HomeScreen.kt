package com.healthanalysis.app.presentation.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.GradientProgressBar
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadData() }
        )
        else -> HomeContent(state)
    }
}

@Composable
private fun HomeContent(state: HomeUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "\u0414\u043E\u0431\u0440\u043E \u043F\u043E\u0436\u0430\u043B\u043E\u0432\u0430\u0442\u044C! \uD83D\uDC4B",
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
            Box {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(RedText)
                        .align(Alignment.TopEnd)
                )
            }
        }

        // Hero Card
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "\u041A\u0430\u043B\u043E\u0440\u0438\u0438 \u0441\u0435\u0433\u043E\u0434\u043D\u044F",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.caloriesConsumed}",
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
                        Text("\u26A1", fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GradientProgressBar(
                    progress = (state.caloriesConsumed.toFloat() / state.caloriesGoal).coerceIn(0f, 1f),
                    height = 8
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\u0426\u0435\u043B\u044C: ${state.caloriesGoal} \u043A\u043A\u0430\u043B",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Grid
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.FavoriteBorder,
                    iconBg = PurpleBg,
                    iconColor = Primary,
                    label = "\u041F\u0443\u043B\u044C\u0441",
                    value = "${state.heartRate} bpm"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.WaterDrop,
                    iconBg = BlueBg,
                    iconColor = BlueText,
                    label = "\u0412\u043E\u0434\u0430",
                    value = "${state.waterGlasses} \u0441\u0442\u0430\u043A."
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.FavoriteBorder,
                    iconBg = PurpleBg,
                    iconColor = Primary,
                    label = "\u0428\u0430\u0433\u0438",
                    value = "${state.steps}"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Flag,
                    iconBg = PurpleBg,
                    iconColor = Primary,
                    label = "\u0426\u0435\u043B\u0438",
                    value = "${state.goalsCompleted}/5"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Chart Section
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "\u041A\u0430\u043B\u043E\u0440\u0438\u0438 \u0437\u0430 \u043D\u0435\u0434\u0435\u043B\u044E",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    label: String,
    value: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun WeeklyCaloriesChart(
    data: List<Float>,
    days: List<String>
) {
    if (data.isEmpty()) return

    val primaryColor = Primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val maxValue = data.maxOrNull() ?: 1f
        val minValue = (data.minOrNull() ?: 0f) * 0.8f
        val range = (maxValue - minValue).coerceAtLeast(1f)
        val paddingBottom = 24f
        val chartHeight = height - paddingBottom

        val stepX = width / (data.size - 1).coerceAtLeast(1)

        val points = data.mapIndexed { index, value ->
            Offset(
                x = index * stepX,
                y = chartHeight - ((value - minValue) / range * chartHeight)
            )
        }

        // Draw line
        val path = Path()
        points.forEachIndexed { index, point ->
            if (index == 0) path.moveTo(point.x, point.y)
            else path.lineTo(point.x, point.y)
        }
        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = primaryColor,
                radius = 4f,
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 2f,
                center = point
            )
        }
    }
}
