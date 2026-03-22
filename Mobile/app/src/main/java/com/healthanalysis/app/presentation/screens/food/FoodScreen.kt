package com.healthanalysis.app.presentation.screens.food

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.data.models.FoodLogResponse
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.GradientProgressBar
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*

@Composable
fun FoodScreen(viewModel: FoodViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadData() }
        )
        else -> FoodContent(state, viewModel)
    }
}

@Composable
private fun FoodContent(state: FoodUiState, viewModel: FoodViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            // Header
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "\u0422\u0440\u0435\u043A\u0435\u0440 \u043F\u0438\u0442\u0430\u043D\u0438\u044F",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u041E\u0442\u0441\u043B\u0435\u0436\u0438\u0432\u0430\u0439\u0442\u0435 \u043A\u0430\u043B\u043E\u0440\u0438\u0438 \u0438 \u043C\u0430\u043A\u0440\u043E\u043D\u0443\u0442\u0440\u0438\u0435\u043D\u0442\u044B",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            // Search Bar
            TextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = {
                    Text(
                        "\u041F\u043E\u0438\u0441\u043A \u043F\u0440\u043E\u0434\u0443\u043A\u0442\u043E\u0432...",
                        color = TextHint,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = TextHint)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Surface,
                    focusedContainerColor = Surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Calorie Card
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Primary.copy(alpha = 0.25f),
                        spotColor = Primary.copy(alpha = 0.25f)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Primary)
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "\u0421\u044A\u0435\u0434\u0435\u043D\u043E \u0441\u0435\u0433\u043E\u0434\u043D\u044F",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                "${state.caloriesConsumed} \u043A\u043A\u0430\u043B",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "\u041E\u0441\u0442\u0430\u043B\u043E\u0441\u044C",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                "${state.caloriesRemaining} \u043A\u043A\u0430\u043B",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    GradientProgressBar(
                        progress = state.caloriesConsumed.toFloat() / state.caloriesGoal,
                        height = 8
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Macros Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MacroCard(
                    modifier = Modifier.weight(1f),
                    name = "\u0411\u0435\u043B\u043A\u0438",
                    value = state.proteins,
                    goal = state.proteinsGoal
                )
                MacroCard(
                    modifier = Modifier.weight(1f),
                    name = "\u0416\u0438\u0440\u044B",
                    value = state.fats,
                    goal = state.fatsGoal
                )
                MacroCard(
                    modifier = Modifier.weight(1f),
                    name = "\u0423\u0433\u043B\u0435\u0432\u043E\u0434\u044B",
                    value = state.carbs,
                    goal = state.carbsGoal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Meal Sections
            MealSection(
                title = "\u0417\u0430\u0432\u0442\u0440\u0430\u043A",
                emoji = "\uD83C\uDF73",
                calories = state.breakfastCalories,
                logs = state.breakfastLogs
            )
            MealSection(
                title = "\u041E\u0431\u0435\u0434",
                emoji = "\uD83C\uDF5C",
                calories = state.lunchCalories,
                logs = state.lunchLogs
            )
            MealSection(
                title = "\u0423\u0436\u0438\u043D",
                emoji = "\uD83C\uDF55",
                calories = state.dinnerCalories,
                logs = state.dinnerLogs
            )
            MealSection(
                title = "\u041F\u0435\u0440\u0435\u043A\u0443\u0441",
                emoji = "\uD83C\uDF4E",
                calories = state.snackCalories,
                logs = state.snackLogs
            )
        }

        // FAB
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .height(56.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Primary.copy(alpha = 0.4f),
                    spotColor = Primary.copy(alpha = 0.4f)
                ),
            containerColor = Primary,
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "+ \u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u043F\u0440\u043E\u0434\u0443\u043A\u0442",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
private fun MacroCard(
    modifier: Modifier = Modifier,
    name: String,
    value: Int,
    goal: Int
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(name, fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${value}\u0433",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(
                            (value.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)
                        )
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Primary)
                )
            }
        }
    }
}

@Composable
private fun MealSection(
    title: String,
    emoji: String,
    calories: Int,
    logs: List<FoodLogResponse>
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(YellowBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Text(
                text = "$calories \u043A\u043A\u0430\u043B",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (logs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "\u041D\u0435\u0442 \u0437\u0430\u043F\u0438\u0441\u0435\u0439",
                    fontSize = 14.sp,
                    color = TextHint
                )
            }
        } else {
            logs.forEach { log ->
                FoodItemCard(log)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FoodItemCard(log: FoodLogResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceLight),
            contentAlignment = Alignment.Center
        ) {
            Text("\uD83C\uDF5E", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = log.product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            val p = ((log.product.proteins * log.weight) / 100.0).toInt()
            val f = ((log.product.fats * log.weight) / 100.0).toInt()
            val c = ((log.product.carbs * log.weight) / 100.0).toInt()
            Text(
                text = "\u0411: ${p}\u0433 \u2022 \u0416: ${f}\u0433 \u2022 \u0423: ${c}\u0433",
                fontSize = 11.sp,
                color = TextHint
            )
        }
        Text(
            text = "${((log.product.calories * log.weight) / 100.0).toInt()} \u043A\u043A\u0430\u043B",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
