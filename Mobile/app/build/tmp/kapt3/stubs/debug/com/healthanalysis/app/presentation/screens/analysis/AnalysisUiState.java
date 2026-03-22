package com.healthanalysis.app.presentation.screens.analysis;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u001a\b\u0086\b\u0018\u00002\u00020\u0001B]\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\u0005H\u00c6\u0003J\t\u0010!\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00c6\u0003Ja\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00c6\u0001J\u0013\u0010$\u001a\u00020\u00032\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\u0007H\u00d6\u0001J\t\u0010\'\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0014R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/healthanalysis/app/presentation/screens/analysis/AnalysisUiState;", "", "isLoading", "", "error", "", "normalCount", "", "lowCount", "highCount", "selectedFilter", "lastUpdate", "items", "", "Lcom/healthanalysis/app/presentation/screens/analysis/AnalysisItem;", "(ZLjava/lang/String;IIILjava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getError", "()Ljava/lang/String;", "getHighCount", "()I", "()Z", "getItems", "()Ljava/util/List;", "getLastUpdate", "getLowCount", "getNormalCount", "getSelectedFilter", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class AnalysisUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    private final int normalCount = 0;
    private final int lowCount = 0;
    private final int highCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedFilter = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String lastUpdate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.healthanalysis.app.presentation.screens.analysis.AnalysisItem> items = null;
    
    public AnalysisUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, int normalCount, int lowCount, int highCount, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedFilter, @org.jetbrains.annotations.NotNull()
    java.lang.String lastUpdate, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.presentation.screens.analysis.AnalysisItem> items) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public final int getNormalCount() {
        return 0;
    }
    
    public final int getLowCount() {
        return 0;
    }
    
    public final int getHighCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSelectedFilter() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLastUpdate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.presentation.screens.analysis.AnalysisItem> getItems() {
        return null;
    }
    
    public AnalysisUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.healthanalysis.app.presentation.screens.analysis.AnalysisItem> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.healthanalysis.app.presentation.screens.analysis.AnalysisUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, int normalCount, int lowCount, int highCount, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedFilter, @org.jetbrains.annotations.NotNull()
    java.lang.String lastUpdate, @org.jetbrains.annotations.NotNull()
    java.util.List<com.healthanalysis.app.presentation.screens.analysis.AnalysisItem> items) {
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