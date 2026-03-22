package com.healthanalysis.app.presentation.screens.profile;

import androidx.lifecycle.ViewModel;
import com.healthanalysis.app.data.local.TokenManager;
import com.healthanalysis.app.data.repository.ProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0011"}, d2 = {"Lcom/healthanalysis/app/presentation/screens/profile/ProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "profileRepository", "Lcom/healthanalysis/app/data/repository/ProfileRepository;", "tokenManager", "Lcom/healthanalysis/app/data/local/TokenManager;", "(Lcom/healthanalysis/app/data/repository/ProfileRepository;Lcom/healthanalysis/app/data/local/TokenManager;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/healthanalysis/app/presentation/screens/profile/ProfileUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadData", "", "logout", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ProfileViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.healthanalysis.app.data.repository.ProfileRepository profileRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.healthanalysis.app.data.local.TokenManager tokenManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.healthanalysis.app.presentation.screens.profile.ProfileUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.healthanalysis.app.presentation.screens.profile.ProfileUiState> uiState = null;
    
    @javax.inject.Inject()
    public ProfileViewModel(@org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.repository.ProfileRepository profileRepository, @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.local.TokenManager tokenManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.healthanalysis.app.presentation.screens.profile.ProfileUiState> getUiState() {
        return null;
    }
    
    public final void loadData() {
    }
    
    public final void logout() {
    }
}