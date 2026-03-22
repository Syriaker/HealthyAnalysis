package com.healthanalysis.app.presentation.screens.food;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.TextFieldDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.healthanalysis.app.data.models.FoodLogResponse;
import com.healthanalysis.app.presentation.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010 \n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\bH\u0003\u001a\u0012\u0010\t\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a*\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0003\u001a.\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u00102\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\b0\u0017H\u0003\u00a8\u0006\u0018"}, d2 = {"FoodContent", "", "state", "Lcom/healthanalysis/app/presentation/screens/food/FoodUiState;", "viewModel", "Lcom/healthanalysis/app/presentation/screens/food/FoodViewModel;", "FoodItemCard", "log", "Lcom/healthanalysis/app/data/models/FoodLogResponse;", "FoodScreen", "MacroCard", "modifier", "Landroidx/compose/ui/Modifier;", "name", "", "value", "", "goal", "MealSection", "title", "emoji", "calories", "logs", "", "app_debug"})
public final class FoodScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void FoodScreen(@org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.presentation.screens.food.FoodViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FoodContent(com.healthanalysis.app.presentation.screens.food.FoodUiState state, com.healthanalysis.app.presentation.screens.food.FoodViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MacroCard(androidx.compose.ui.Modifier modifier, java.lang.String name, int value, int goal) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MealSection(java.lang.String title, java.lang.String emoji, int calories, java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> logs) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FoodItemCard(com.healthanalysis.app.data.models.FoodLogResponse log) {
    }
}