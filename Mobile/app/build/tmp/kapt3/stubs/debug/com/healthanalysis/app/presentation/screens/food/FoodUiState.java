package com.healthanalysis.app.presentation.screens.food;

import androidx.lifecycle.ViewModel;
import com.healthanalysis.app.data.models.FoodLogResponse;
import com.healthanalysis.app.data.repository.NutritionRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b9\b\u0086\b\u0018\u00002\u00020\u0001B\u00e7\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\b\u0012\b\b\u0002\u0010\u000b\u001a\u00020\b\u0012\b\b\u0002\u0010\f\u001a\u00020\b\u0012\b\b\u0002\u0010\r\u001a\u00020\b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\b\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\b\b\u0002\u0010\u0017\u001a\u00020\b\u0012\b\b\u0002\u0010\u0018\u001a\u00020\b\u0012\b\b\u0002\u0010\u0019\u001a\u00020\b\u0012\b\b\u0002\u0010\u001a\u001a\u00020\b\u00a2\u0006\u0002\u0010\u001bJ\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\bH\u00c6\u0003J\t\u00105\u001a\u00020\bH\u00c6\u0003J\t\u00106\u001a\u00020\bH\u00c6\u0003J\u000f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u00c6\u0003J\u000f\u00108\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u00c6\u0003J\u000f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u00c6\u0003J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u00c6\u0003J\t\u0010;\u001a\u00020\bH\u00c6\u0003J\t\u0010<\u001a\u00020\bH\u00c6\u0003J\t\u0010=\u001a\u00020\bH\u00c6\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010?\u001a\u00020\bH\u00c6\u0003J\t\u0010@\u001a\u00020\u0005H\u00c6\u0003J\t\u0010A\u001a\u00020\bH\u00c6\u0003J\t\u0010B\u001a\u00020\bH\u00c6\u0003J\t\u0010C\u001a\u00020\bH\u00c6\u0003J\t\u0010D\u001a\u00020\bH\u00c6\u0003J\t\u0010E\u001a\u00020\bH\u00c6\u0003J\t\u0010F\u001a\u00020\bH\u00c6\u0003J\u00eb\u0001\u0010G\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\b2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\b\b\u0002\u0010\u0017\u001a\u00020\b2\b\b\u0002\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0019\u001a\u00020\b2\b\b\u0002\u0010\u001a\u001a\u00020\bH\u00c6\u0001J\u0013\u0010H\u001a\u00020\u00032\b\u0010I\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010J\u001a\u00020\bH\u00d6\u0001J\t\u0010K\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0017\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u0011\u0010\n\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001dR\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001dR\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001dR\u0011\u0010\u0010\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001dR\u0011\u0010\u0019\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001dR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\r\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001dR\u0011\u0010\u000e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001dR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010+R\u0011\u0010\u0018\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001dR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001fR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001dR\u0011\u0010\f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001dR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010(R\u0011\u0010\u001a\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001dR\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001f\u00a8\u0006L"}, d2 = {"Lcom/healthanalysis/app/presentation/screens/food/FoodUiState;", "", "isLoading", "", "error", "", "searchQuery", "caloriesConsumed", "", "caloriesRemaining", "caloriesGoal", "proteins", "proteinsGoal", "fats", "fatsGoal", "carbs", "carbsGoal", "breakfastLogs", "", "Lcom/healthanalysis/app/data/models/FoodLogResponse;", "lunchLogs", "dinnerLogs", "snackLogs", "breakfastCalories", "lunchCalories", "dinnerCalories", "snackCalories", "(ZLjava/lang/String;Ljava/lang/String;IIIIIIIIILjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;IIII)V", "getBreakfastCalories", "()I", "getBreakfastLogs", "()Ljava/util/List;", "getCaloriesConsumed", "getCaloriesGoal", "getCaloriesRemaining", "getCarbs", "getCarbsGoal", "getDinnerCalories", "getDinnerLogs", "getError", "()Ljava/lang/String;", "getFats", "getFatsGoal", "()Z", "getLunchCalories", "getLunchLogs", "getProteins", "getProteinsGoal", "getSearchQuery", "getSnackCalories", "getSnackLogs", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class FoodUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String searchQuery = null;
    private final int caloriesConsumed = 0;
    private final int caloriesRemaining = 0;
    private final int caloriesGoal = 0;
    private final int proteins = 0;
    private final int proteinsGoal = 0;
    private final int fats = 0;
    private final int fatsGoal = 0;
    private final int carbs = 0;
    private final int carbsGoal = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> breakfastLogs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> lunchLogs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> dinnerLogs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> snackLogs = null;
    private final int breakfastCalories = 0;
    private final int lunchCalories = 0;
    private final int dinnerCalories = 0;
    private final int snackCalories = 0;
    
    public FoodUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, int caloriesConsumed, int caloriesRemaining, int caloriesGoal, int proteins, int proteinsGoal, int fats, int fatsGoal, int carbs, int carbsGoal, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> breakfastLogs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> lunchLogs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> dinnerLogs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> snackLogs, int breakfastCalories, int lunchCalories, int dinnerCalories, int snackCalories) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    public final int getCaloriesConsumed() {
        return 0;
    }
    
    public final int getCaloriesRemaining() {
        return 0;
    }
    
    public final int getCaloriesGoal() {
        return 0;
    }
    
    public final int getProteins() {
        return 0;
    }
    
    public final int getProteinsGoal() {
        return 0;
    }
    
    public final int getFats() {
        return 0;
    }
    
    public final int getFatsGoal() {
        return 0;
    }
    
    public final int getCarbs() {
        return 0;
    }
    
    public final int getCarbsGoal() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> getBreakfastLogs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> getLunchLogs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> getDinnerLogs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> getSnackLogs() {
        return null;
    }
    
    public final int getBreakfastCalories() {
        return 0;
    }
    
    public final int getLunchCalories() {
        return 0;
    }
    
    public final int getDinnerCalories() {
        return 0;
    }
    
    public final int getSnackCalories() {
        return 0;
    }
    
    public FoodUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> component16() {
        return null;
    }
    
    public final int component17() {
        return 0;
    }
    
    public final int component18() {
        return 0;
    }
    
    public final int component19() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component20() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.healthanalysis.app.presentation.screens.food.FoodUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, int caloriesConsumed, int caloriesRemaining, int caloriesGoal, int proteins, int proteinsGoal, int fats, int fatsGoal, int carbs, int carbsGoal, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> breakfastLogs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> lunchLogs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> dinnerLogs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.data.models.FoodLogResponse> snackLogs, int breakfastCalories, int lunchCalories, int dinnerCalories, int snackCalories) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}