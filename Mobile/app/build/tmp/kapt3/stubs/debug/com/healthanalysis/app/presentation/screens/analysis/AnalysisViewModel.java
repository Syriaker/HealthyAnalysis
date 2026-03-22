package com.healthanalysis.app.presentation.screens.analysis;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u000f"}, d2 = {"Lcom/healthanalysis/app/presentation/screens/analysis/AnalysisViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/healthanalysis/app/presentation/screens/analysis/AnalysisUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadData", "", "setFilter", "filter", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AnalysisViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.healthanalysis.app.presentation.screens.analysis.AnalysisUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.healthanalysis.app.presentation.screens.analysis.AnalysisUiState> uiState = null;
    
    @javax.inject.Inject()
    public AnalysisViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.healthanalysis.app.presentation.screens.analysis.AnalysisUiState> getUiState() {
        return null;
    }
    
    public final void loadData() {
    }
    
    public final void setFilter(@org.jetbrains.annotations.NotNull()
    java.lang.String filter) {
    }
}