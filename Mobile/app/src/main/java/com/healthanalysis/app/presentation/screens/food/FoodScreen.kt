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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
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
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.healthanalysis.app.presentation.components.ErrorScreen
import com.healthanalysis.app.presentation.components.LoadingScreen
import com.healthanalysis.app.presentation.theme.*
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

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

@Composable
fun FoodScreen(
    viewModel: FoodViewModel = hiltViewModel(),
    onBarcodeScanned: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { barcode -> onBarcodeScanned(barcode) }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val barcode = decodeBarcodeFromUri(context, it)
            if (barcode != null) {
                Toast.makeText(context, "\u0428\u0442\u0440\u0438\u0445-\u043A\u043E\u0434: $barcode", Toast.LENGTH_SHORT).show()
                onBarcodeScanned(barcode)
            } else {
                Toast.makeText(context, "\u0428\u0442\u0440\u0438\u0445-\u043A\u043E\u0434 \u043D\u0435 \u0440\u0430\u0441\u043F\u043E\u0437\u043D\u0430\u043D", Toast.LENGTH_SHORT).show()
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
            viewModel = viewModel,
            onScanBarcode = {
                scanLauncher.launch(
                    ScanOptions().apply {
                        setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                        setPrompt("\u041D\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u043A\u0430\u043C\u0435\u0440\u0443 \u043D\u0430 \u0448\u0442\u0440\u0438\u0445-\u043A\u043E\u0434")
                        setBeepEnabled(true)
                        setOrientationLocked(true)
                    }
                )
            },
            onPickPhoto = { imagePickerLauncher.launch("image/*") }
        )
    }

    if (state.showAddDialog) {
        AddFoodDialog(
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
    viewModel: FoodViewModel,
    onScanBarcode: () -> Unit,
    onPickPhoto: () -> Unit
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
                    value = state.proteins
                )
                MacroCard(
                    modifier = Modifier.weight(1f),
                    name = "\u0416\u0438\u0440\u044B",
                    value = state.fats
                )
                MacroCard(
                    modifier = Modifier.weight(1f),
                    name = "\u0423\u0433\u043B\u0435\u0432\u043E\u0434\u044B",
                    value = state.carbs
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Meal Sections
            MealSection(
                title = "\u0417\u0430\u0432\u0442\u0440\u0430\u043A",
                emoji = "\uD83C\uDF73",
                calories = state.breakfastCalories,
                items = state.breakfastItems
            )
            MealSection(
                title = "\u041E\u0431\u0435\u0434",
                emoji = "\uD83C\uDF5C",
                calories = state.lunchCalories,
                items = state.lunchItems
            )
            MealSection(
                title = "\u0423\u0436\u0438\u043D",
                emoji = "\uD83C\uDF55",
                calories = state.dinnerCalories,
                items = state.dinnerItems
            )
            MealSection(
                title = "\u041F\u0435\u0440\u0435\u043A\u0443\u0441",
                emoji = "\uD83C\uDF4E",
                calories = state.snackCalories,
                items = state.snackItems
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
                text = "\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C\n\u043F\u0440\u043E\u0434\u0443\u043A\u0442",
                onClick = { viewModel.showAddDialog() }
            )
            ActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.CameraAlt,
                text = "\u0421\u043A\u0430\u043D\u0438\u0440\u043E\u0432\u0430\u0442\u044C\n\u0448\u0442\u0440\u0438\u0445-\u043A\u043E\u0434",
                onClick = onScanBarcode
            )
            ActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.PhotoLibrary,
                text = "\u0424\u043E\u0442\u043E\n\u0448\u0442\u0440\u0438\u0445-\u043A\u043E\u0434\u0430",
                onClick = onPickPhoto
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
                "${value}\u0433",
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
    items: List<DisplayFoodItem>
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
                    "\u041D\u0435\u0442 \u0437\u0430\u043F\u0438\u0441\u0435\u0439",
                    fontSize = 14.sp,
                    color = TextHint
                )
            }
        } else {
            items.forEach { item ->
                FoodItemCard(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FoodItemCard(item: DisplayFoodItem) {
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
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "\u0411: ${item.proteins}\u0433 \u2022 \u0416: ${item.fats}\u0433 \u2022 \u0423: ${item.carbs}\u0433",
                fontSize = 11.sp,
                color = TextHint
            )
        }
        Text(
            text = "${item.calories} \u043A\u043A\u0430\u043B",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
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
    onDismiss: () -> Unit,
    onAdd: (name: String, calories: Int, proteins: Int, fats: Int, carbs: Int, mealType: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("breakfast") }

    val mealTypes = listOf(
        "breakfast" to "\u0417\u0430\u0432\u0442\u0440\u0430\u043A",
        "lunch" to "\u041E\u0431\u0435\u0434",
        "dinner" to "\u0423\u0436\u0438\u043D",
        "snack" to "\u041F\u0435\u0440\u0435\u043A\u0443\u0441"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u043F\u0440\u043E\u0434\u0443\u043A\u0442",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("\u041D\u0430\u0437\u0432\u0430\u043D\u0438\u0435") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { c -> c.isDigit() } },
                    label = { Text("\u041A\u0430\u043B\u043E\u0440\u0438\u0438") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = proteins,
                        onValueChange = { proteins = it.filter { c -> c.isDigit() } },
                        label = { Text("\u0411\u0435\u043B\u043A\u0438") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = fats,
                        onValueChange = { fats = it.filter { c -> c.isDigit() } },
                        label = { Text("\u0416\u0438\u0440\u044B") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it.filter { c -> c.isDigit() } },
                        label = { Text("\u0423\u0433\u043B\u0435\u0432.") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "\u041F\u0440\u0438\u0451\u043C \u043F\u0438\u0449\u0438",
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
                    val cal = calories.toIntOrNull() ?: 0
                    val prot = proteins.toIntOrNull() ?: 0
                    val fat = fats.toIntOrNull() ?: 0
                    val carb = carbs.toIntOrNull() ?: 0
                    if (name.isNotBlank()) {
                        onAdd(name, cal, prot, fat, carb, selectedMealType)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotBlank()
            ) {
                Text("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("\u041E\u0442\u043C\u0435\u043D\u0430", color = TextSecondary)
            }
        }
    )
}
