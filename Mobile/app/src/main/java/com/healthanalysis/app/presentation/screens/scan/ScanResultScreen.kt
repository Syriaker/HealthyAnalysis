package com.healthanalysis.app.presentation.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*

private val mealTypes = listOf(
    "breakfast" to "Завтрак",
    "lunch" to "Обед",
    "dinner" to "Ужин",
    "snack" to "Перекус"
)

@Composable
fun ScanResultScreen(
    viewModel: ScanResultViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.logSuccess) {
        if (state.logSuccess) onBack()
    }

    when {
        state.isLoading -> LoadingScreen()
        state.error != null && state.product == null -> ErrorContent(
            message = state.error!!,
            onBack = onBack
        )
        state.product != null -> ProductContent(
            state = state,
            onWeightChanged = viewModel::onWeightChanged,
            onMealTypeChanged = viewModel::onMealTypeChanged,
            onLogFood = viewModel::logFood,
            onBack = onBack
        )
    }
}

@Composable
private fun ErrorContent(message: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Продукт не найден", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, fontSize = 14.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Назад")
        }
    }
}

@Composable
private fun ProductContent(
    state: ScanResultUiState,
    onWeightChanged: (String) -> Unit,
    onMealTypeChanged: (String) -> Unit,
    onLogFood: () -> Unit,
    onBack: () -> Unit
) {
    val product = state.product!!

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
                .padding(start = 16.dp, end = 24.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextPrimary)
            }
            Text("Продукт", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Card
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Штрих-код: ${product.barcode}",
                    fontSize = 13.sp,
                    color = TextHint
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Nutrition per 100g
                Text(
                    "На 100 г",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NutrientChip("Ккал", "%.0f".format(product.calories), PurpleBg, Primary)
                    NutrientChip("Белки", "%.1f".format(product.proteins), GreenBg, GreenText)
                    NutrientChip("Жиры", "%.1f".format(product.fats), YellowBg, YellowText)
                    NutrientChip("Углев.", "%.1f".format(product.carbs), BlueBg, BlueText)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weight input
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                "Вес (граммы)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.weight,
                onValueChange = { onWeightChanged(it.filter { c -> c.isDigit() }) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Border
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Meal type selector
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                "Приём пищи",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mealTypes.forEach { (key, label) ->
                    val selected = state.selectedMealType == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) Primary else Color.White)
                            .clickable { onMealTypeChanged(key) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) Color.White else TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calculated nutrition
        val weightMultiplier = (state.weight.toIntOrNull() ?: 0) / 100.0
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(PurpleBg)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    "Итого (${state.weight} г)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("%.0f ккал".format(product.calories * weightMultiplier), fontSize = 13.sp, color = TextPrimary)
                    Text("Б: %.1f".format(product.proteins * weightMultiplier), fontSize = 13.sp, color = TextPrimary)
                    Text("Ж: %.1f".format(product.fats * weightMultiplier), fontSize = 13.sp, color = TextPrimary)
                    Text("У: %.1f".format(product.carbs * weightMultiplier), fontSize = 13.sp, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Log button
        Button(
            onClick = onLogFood,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            enabled = !state.isLogging && (state.weight.toIntOrNull() ?: 0) > 0,
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (state.isLogging) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Добавить в дневник", fontWeight = FontWeight.Bold)
            }
        }

        if (state.error != null && state.product != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                state.error,
                color = RedText,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun NutrientChip(label: String, value: String, bg: Color, textColor: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
        Text(label, fontSize = 11.sp, color = textColor.copy(alpha = 0.8f))
    }
}
