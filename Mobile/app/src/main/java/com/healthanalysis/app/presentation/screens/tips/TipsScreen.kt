package com.healthanalysis.app.presentation.screens.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*

@Composable
fun TipsScreen(viewModel: TipsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadData() }
        )
        else -> TipsContent(state)
    }
}

@Composable
private fun TipsContent(state: TipsUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "\u0420\u0435\u043A\u043E\u043C\u0435\u043D\u0434\u0430\u0446\u0438\u0438",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "\u041F\u0435\u0440\u0441\u043E\u043D\u0430\u043B\u044C\u043D\u044B\u0435 \u0441\u043E\u0432\u0435\u0442\u044B \u043D\u0430 \u043E\u0441\u043D\u043E\u0432\u0435 \u0432\u0430\u0448\u0438\u0445 \u0434\u0430\u043D\u043D\u044B\u0445",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        // Top Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopStatCard(
                modifier = Modifier.weight(1f),
                bgColor = RedBg,
                iconColor = RedText,
                icon = "!",
                count = state.importantCount,
                label = "\u0412\u0430\u0436\u043D\u044B\u0435"
            )
            TopStatCard(
                modifier = Modifier.weight(1f),
                bgColor = YellowBg,
                iconColor = YellowText,
                icon = "?",
                count = state.tipsCount,
                label = "\u0421\u043E\u0432\u0435\u0442\u044B"
            )
            TopStatCard(
                modifier = Modifier.weight(1f),
                bgColor = GreenBg,
                iconColor = GreenText,
                icon = "\u2713",
                count = state.successCount,
                label = "\u0423\u0441\u043F\u0435\u0445\u0438"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tip Cards
        state.tips.forEach { tip ->
            TipCard(tip)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TopStatCard(
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
            Text(label, fontSize = 12.sp, color = TextSecondary)
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
        // Priority badge
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
                text = if (tip.priority == "high") "\u0412\u0430\u0436\u043D\u043E" else "\u0421\u0440\u0435\u0434\u043D\u0435",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (tip.priority == "high") RedText else YellowText
            )
        }

        Column(modifier = Modifier.padding(end = 70.dp)) {
            // Emoji icon
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

            Text(
                tip.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                tip.description,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        tip.primaryAction,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                if (tip.secondaryAction != null) {
                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            tip.secondaryAction,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                }
            }
        }
    }
}
