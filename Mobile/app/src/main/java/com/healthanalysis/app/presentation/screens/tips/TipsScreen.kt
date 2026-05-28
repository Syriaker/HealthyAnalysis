package com.healthanalysis.app.presentation.screens.tips

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TipsScreen(viewModel: TipsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    TipsContent(state, onAnalyze = { viewModel.analyzeMetrics() })
}

@Composable
private fun TipsContent(state: TipsUiState, onAnalyze: () -> Unit) {
    var showHistory by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<AiHistoryEntry?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "Рекомендации",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Персональные советы на основе ваших данных",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        // AI Analysis card
        AiAnalysisCard(state, onAnalyze)

        Spacer(modifier = Modifier.height(12.dp))

        // Disclaimer
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(YellowBg)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("⚠️", fontSize = 14.sp)
            Text(
                "Ответы ИИ носят исключительно рекомендательный характер. Для точной диагностики и лечения обратитесь к врачу.",
                fontSize = 12.sp,
                color = YellowText,
                lineHeight = 18.sp
            )
        }

        if (state.aiHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            AiHistoryToggle(
                history = state.aiHistory,
                showHistory = showHistory,
                onToggle = { showHistory = !showHistory },
                onSelect = { selectedEntry = it }
            )
        }

        // Tip Cards
        state.tips.forEach { tip ->
            Spacer(modifier = Modifier.height(16.dp))
            TipCard(tip)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    selectedEntry?.let { entry ->
        HistoryDialog(entry = entry, onDismiss = { selectedEntry = null })
    }
}

@Composable
private fun AiAnalysisCard(state: TipsUiState, onAnalyze: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BlueBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤖", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "ИИ-Анализ показателей",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        "На основе данных за последние 3 дня",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            if (state.isAnalyzing) {
                AiLoadingAnimation()
            } else {
                if (state.aiAdvice != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (state.aiDate != null) {
                        Text(
                            "Анализ от ${formatDate(state.aiDate)}",
                            fontSize = 12.sp,
                            color = TextHint
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        state.aiAdvice,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                }

                if (state.analyzeError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(state.analyzeError, fontSize = 13.sp, color = RedText)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAnalyze,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        "Проанализировать показатели",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun AiHistoryToggle(
    history: List<AiHistoryEntry>,
    showHistory: Boolean,
    onToggle: () -> Unit,
    onSelect: (AiHistoryEntry) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Column {
            // Toggle row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📋", fontSize = 17.sp)
                    Text(
                        "История анализов",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Surface)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "${history.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                    Text(
                        if (showHistory) "▲" else "▼",
                        fontSize = 11.sp,
                        color = TextHint
                    )
                }
            }

            if (showHistory) {
                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Surface)
                )

                history.forEachIndexed { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(entry) }
                            .padding(horizontal = 16.dp, vertical = 13.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("📅", fontSize = 15.sp)
                            Text(
                                if (entry.date.isNotEmpty()) formatDate(entry.date) else "Без даты",
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                        }
                        if (index == 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BlueBg)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    "Последний",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = BlueText
                                )
                            }
                        } else {
                            Text("›", fontSize = 18.sp, color = TextHint)
                        }
                    }
                    if (index < history.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Surface)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryDialog(entry: AiHistoryEntry, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Анализ ИИ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (entry.date.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                formatDate(entry.date),
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Surface)
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✕", fontSize = 13.sp, color = TextSecondary)
                    }
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Surface)
                )

                // Scrollable text
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    Text(
                        entry.text,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Surface)
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        "Закрыть",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun AiLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "ai")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )
    val brainScale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val pulse2 = (pulse + 0.5f) % 1f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxR = size.minDimension / 2f
                listOf(pulse, pulse2).forEach { p ->
                    drawCircle(
                        color = Primary.copy(alpha = (1f - p) * 0.4f),
                        radius = maxR * (0.35f + p * 0.65f),
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
            Text("🧠", fontSize = 38.sp, modifier = Modifier.scale(brainScale))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "ИИ анализирует ваши данные...",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Обычно занимает 20–40 секунд",
            fontSize = 13.sp,
            color = TextSecondary
        )
    }
}

private fun formatDate(dateStr: String): String {
    return try {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateStr)
            ?: return dateStr.take(10)
        SimpleDateFormat("d MMMM yyyy", Locale("ru")).format(date)
    } catch (e: Exception) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
                ?: return dateStr.take(10)
            SimpleDateFormat("d MMMM yyyy", Locale("ru")).format(date)
        } catch (e2: Exception) {
            dateStr.take(10)
        }
    }
}

@Composable
private fun TipCard(tip: TipItem) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(8.dp))
                .then(
                    if (tip.priority == "high")
                        Modifier.border(1.dp, RedText, RoundedCornerShape(8.dp))
                    else
                        Modifier.border(1.dp, YellowText, RoundedCornerShape(8.dp))
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (tip.priority == "high") "Важно" else "Среднее",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (tip.priority == "high") RedText else YellowText
            )
        }

        Column(modifier = Modifier.padding(end = 70.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Surface),
                contentAlignment = Alignment.Center
            ) {
                Text(tip.emoji, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(tip.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(tip.description, fontSize = 14.sp, color = TextSecondary, lineHeight = 21.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(tip.primaryAction, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                if (tip.secondaryAction != null) {
                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(tip.secondaryAction, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }
        }
    }
}
