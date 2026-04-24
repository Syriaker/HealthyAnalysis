package com.healthanalysis.app.presentation.screens.food

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.time.DayOfWeek
import java.time.LocalDate

private fun decodeBarcodeFromUri(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        MultiFormatReader().decode(binaryBitmap).text
    } catch (e: Exception) {
        null
    }
}

private fun calorieLabelForDate(date: LocalDate): String {
    val today = LocalDate.now()
    if (date == today) return "Съедено сегодня"
    if (date == today.minusDays(1)) return "Съедено вчера"
    val months = arrayOf(
        "янв", "фев", "мар", "апр",
        "мая", "июн", "июл", "авг",
        "сен", "окт", "ноя", "дек"
    )
    return "Съедено ${date.dayOfMonth} ${months[date.monthValue - 1]}"
}

@Composable
fun FoodScreen(
    viewModel: FoodViewModel = hiltViewModel(),
    onBarcodeScanned: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Перезагружаем данные при каждом возврате на экран (в т.ч. после ScanResultScreen).
    LifecycleResumeEffect(Unit) {
        viewModel.loadData()
        onPauseOrDispose {}
    }

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { barcode -> onBarcodeScanned(barcode) }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val barcode = decodeBarcodeFromUri(context, it)
            if (barcode != null) {
                Toast.makeText(context, "Штрих-код: $barcode", Toast.LENGTH_SHORT).show()
                onBarcodeScanned(barcode)
            } else {
                Toast.makeText(context, "Штрих-код не распознан", Toast.LENGTH_SHORT).show()
            }
        }
    }

    when {
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadData() }
        )
        else -> FoodContent(
            state = state,
            onScanBarcode = {
                scanLauncher.launch(
                    ScanOptions().apply {
                        setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                        setPrompt("Наведите камеру на штрих-код")
                        setBeepEnabled(true)
                        setOrientationLocked(true)
                    }
                )
            },
            onPickPhoto = { imagePickerLauncher.launch("image/*") },
            onPreviousWeek = { viewModel.previousWeek() },
            onNextWeek = { viewModel.nextWeek() },
            onDaySelected = { viewModel.selectDay(it) },
            onShowAddDialog = { viewModel.showAddDialog() },
            onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
            onDeleteItem = { mealType, index -> viewModel.removeEntry(mealType, index) }
        )
    }

    if (state.showAddDialog) {
        AddFoodDialog(
            isAdding = state.isAddingFood,
            addError = state.addError,
            onDismiss = { viewModel.hideAddDialog() },
            onAdd = { name, cal, prot, fat, carb, meal ->
                viewModel.addManualEntry(name, cal, prot, fat, carb, meal)
            }
        )
    }
}

@Composable
private fun FoodContent(
    state: FoodUiState,
    onScanBarcode: () -> Unit,
    onPickPhoto: () -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onDaySelected: (LocalDate) -> Unit,
    onShowAddDialog: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onDeleteItem: (mealType: String, index: Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            // Header
            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)) {
                Text(
                    text = "Трекер питания",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Отслеживайте калории и макронутриенты",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            // Week Calendar
            WeekCalendar(
                selectedDate = state.selectedDate,
                onDaySelected = onDaySelected,
                onPreviousWeek = onPreviousWeek,
                onNextWeek = onNextWeek
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            TextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = {
                    Text(
                        "Поиск продуктов...",
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
                    Text(
                        calorieLabelForDate(state.selectedDate),
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        "${state.caloriesConsumed} ккал",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                MacroCard(modifier = Modifier.weight(1f), name = "Белки", value = state.proteins)
                MacroCard(modifier = Modifier.weight(1f), name = "Жиры", value = state.fats)
                MacroCard(modifier = Modifier.weight(1f), name = "Углеводы", value = state.carbs)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Meal Sections
            MealSection(
                title = "Завтрак", emoji = "🍳",
                calories = state.breakfastCalories, items = state.breakfastItems,
                onDeleteItem = { index -> onDeleteItem("breakfast", index) }
            )
            MealSection(
                title = "Обед", emoji = "🍜",
                calories = state.lunchCalories, items = state.lunchItems,
                onDeleteItem = { index -> onDeleteItem("lunch", index) }
            )
            MealSection(
                title = "Ужин", emoji = "🍕",
                calories = state.dinnerCalories, items = state.dinnerItems,
                onDeleteItem = { index -> onDeleteItem("dinner", index) }
            )
            MealSection(
                title = "Перекус", emoji = "🍎",
                calories = state.snackCalories, items = state.snackItems,
                onDeleteItem = { index -> onDeleteItem("snack", index) }
            )
        }

        // Bottom Buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Edit,
                text = "Добавить\nпродукт",
                onClick = onShowAddDialog
            )
            ActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.CameraAlt,
                text = "Сканировать\nштрих-код",
                onClick = onScanBarcode
            )
            ActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.PhotoLibrary,
                text = "Фото\nштрих-кода",
                onClick = onPickPhoto
            )
        }
    }
}

@Composable
private fun WeekCalendar(
    selectedDate: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    val today = LocalDate.now()
    val monday = selectedDate.with(DayOfWeek.MONDAY)
    val weekDays = (0..6).map { monday.plusDays(it.toLong()) }
    val dayNames = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    val todayMonday = today.with(DayOfWeek.MONDAY)
    val canGoForward = monday.isBefore(todayMonday)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousWeek,
            modifier = Modifier.size(36.dp)
        ) {
            Text("←", fontSize = 20.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
        }

        weekDays.forEachIndexed { index, date ->
            val isSelected = date == selectedDate
            val isFuture = date.isAfter(today)
            val isToday = date == today

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Primary else Color.Transparent)
                    .then(
                        if (!isFuture) Modifier.clickable { onDaySelected(date) }
                        else Modifier
                    )
                    .padding(vertical = 6.dp, horizontal = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dayNames[index],
                    fontSize = 10.sp,
                    color = when {
                        isSelected -> Color.White
                        isFuture -> TextHint
                        else -> TextSecondary
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${date.dayOfMonth}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isSelected -> Color.White
                        isFuture -> TextHint
                        else -> TextPrimary
                    }
                )
                if (isToday && !isSelected) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(Primary)
                    )
                }
            }
        }

        IconButton(
            onClick = onNextWeek,
            enabled = canGoForward,
            modifier = Modifier.size(36.dp)
        ) {
            Text(
                "→",
                fontSize = 20.sp,
                color = if (canGoForward) TextPrimary else TextHint,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MacroCard(
    modifier: Modifier = Modifier,
    name: String,
    value: Int
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
                "${value}г",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
    }
}

@Composable
private fun MealSection(
    title: String,
    emoji: String,
    calories: Int,
    items: List<DisplayFoodItem>,
    onDeleteItem: (Int) -> Unit
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
                text = "$calories ккал",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Нет записей",
                    fontSize = 14.sp,
                    color = TextHint
                )
            }
        } else {
            items.forEachIndexed { index, item ->
                FoodItemCard(item, onDelete = { onDeleteItem(index) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FoodItemCard(item: DisplayFoodItem, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceLight),
            contentAlignment = Alignment.Center
        ) {
            Text("🍞", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Б: ${item.proteins}г • Ж: ${item.fats}г • У: ${item.carbs}г",
                fontSize = 11.sp,
                color = TextHint
            )
        }
        Text(
            text = "${item.calories} ккал",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Удалить",
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Primary)
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 2,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun AddFoodDialog(
    isAdding: Boolean,
    addError: String?,
    onDismiss: () -> Unit,
    onAdd: (name: String, calories: Int, proteins: Int, fats: Int, carbs: Int, mealType: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("breakfast") }
    var submitAttempted by remember { mutableStateOf(false) }

    val isValid = name.isNotBlank() && calories.isNotBlank() && proteins.isNotBlank() && fats.isNotBlank() && carbs.isNotBlank()

    val mealTypes = listOf(
        "breakfast" to "Завтрак",
        "lunch" to "Обед",
        "dinner" to "Ужин",
        "snack" to "Перекус"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Добавить продукт",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    isError = submitAttempted && name.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { c -> c.isDigit() } },
                    label = { Text("Калории") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    isError = submitAttempted && calories.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = proteins,
                        onValueChange = { proteins = it.filter { c -> c.isDigit() } },
                        label = { Text("Белки") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = submitAttempted && proteins.isBlank()
                    )
                    OutlinedTextField(
                        value = fats,
                        onValueChange = { fats = it.filter { c -> c.isDigit() } },
                        label = { Text("Жиры") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = submitAttempted && fats.isBlank()
                    )
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it.filter { c -> c.isDigit() } },
                        label = { Text("Углев.") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = submitAttempted && carbs.isBlank()
                    )
                }
                if (submitAttempted && !isValid) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Заполните все поля",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Приём пищи",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    mealTypes.forEach { (key, label) ->
                        val selected = selectedMealType == key
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selected) Primary else Surface)
                                .clickable { selectedMealType = key }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                label,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    submitAttempted = true
                    if (isValid) {
                        onAdd(
                            name,
                            calories.toInt(),
                            proteins.toInt(),
                            fats.toInt(),
                            carbs.toInt(),
                            selectedMealType
                        )
                    }
                },
                enabled = !isAdding,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isAdding) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Добавить")
                }
            }
        },
        dismissButton = {
            Column(horizontalAlignment = Alignment.End) {
                if (addError != null) {
                    Text(
                        addError,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(end = 8.dp, bottom = 4.dp)
                    )
                }
                TextButton(onClick = onDismiss, enabled = !isAdding) {
                    Text("Отмена", color = TextSecondary)
                }
            }
        }
    )
}
