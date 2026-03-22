package com.healthanalysis.app.presentation.screens.home;

import androidx.lifecycle.ViewModel;
import com.healthanalysis.app.data.repository.NutritionRepository;
import com.healthanalysis.app.data.repository.ProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0010\u0007\n\u0002\b\u001f\b\u0086\b\u0018\u00002\u00020\u0001Bw\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\f\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e\u00a2\u0006\u0002\u0010\u0011J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00050\u000eH\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0007H\u00c6\u0003J\t\u0010#\u001a\u00020\u0007H\u00c6\u0003J\t\u0010$\u001a\u00020\u0007H\u00c6\u0003J\t\u0010%\u001a\u00020\u0007H\u00c6\u0003J\t\u0010&\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0003J{\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00072\b\b\u0002\u0010\f\u001a\u00020\u00072\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000eH\u00c6\u0001J\u0013\u0010*\u001a\u00020\u00032\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020\u0007H\u00d6\u0001J\t\u0010-\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\f\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0019R\u0011\u0010\u000b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0013R\u0011\u0010\n\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001d\u00a8\u0006."}, d2 = {"Lcom/healthanalysis/app/presentation/screens/home/HomeUiState;", "", "isLoading", "", "error", "", "caloriesConsumed", "", "caloriesGoal", "heartRate", "waterGlasses", "steps", "goalsCompleted", "weeklyCalories", "", "", "weekDays", "(ZLjava/lang/String;IIIIIILjava/util/List;Ljava/util/List;)V", "getCaloriesConsumed", "()I", "getCaloriesGoal", "getError", "()Ljava/lang/String;", "getGoalsCompleted", "getHeartRate", "()Z", "getSteps", "getWaterGlasses", "getWeekDays", "()Ljava/util/List;", "getWeeklyCalories", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class HomeUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    private final int caloriesConsumed = 0;
    private final int caloriesGoal = 0;
    private final int heartRate = 0;
    private final int waterGlasses = 0;
    private final int steps = 0;
    private final int goalsCompleted = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Float> weeklyCalories = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> weekDays = null;
    
    public HomeUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, int caloriesConsumed, int caloriesGoal, int heartRate, int waterGlasses, int steps, int goalsCompleted, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> weeklyCalories, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> weekDays) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public final int getCaloriesConsumed() {
        return 0;
    }
    
    public final int getCaloriesGoal() {
        return 0;
    }
    
    public final int getHeartRate() {
        return 0;
    }
    
    public final int getWaterGlasses() {
        return 0;
    }
    
    public final int getSteps() {
        return 0;
    }
    
    public final int getGoalsCompleted() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> getWeeklyCalories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getWeekDays() {
        return null;
    }
    
    public HomeUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.healthanalysis.app.presentation.screens.home.HomeUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, int caloriesConsumed, int caloriesGoal, int heartRate, int waterGlasses, int steps, int goalsCompleted, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> weeklyCalories, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> weekDays) {
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