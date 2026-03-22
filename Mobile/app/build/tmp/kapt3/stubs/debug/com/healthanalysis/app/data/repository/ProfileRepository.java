package com.healthanalysis.app.data.repository;

import com.healthanalysis.app.data.api.ProfileApi;
import com.healthanalysis.app.data.models.ProfileResponse;
import com.healthanalysis.app.data.models.ProfileUpdateRequest;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\b\u0010\tJ$\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u000f"}, d2 = {"Lcom/healthanalysis/app/data/repository/ProfileRepository;", "", "profileApi", "Lcom/healthanalysis/app/data/api/ProfileApi;", "(Lcom/healthanalysis/app/data/api/ProfileApi;)V", "getProfile", "Lkotlin/Result;", "Lcom/healthanalysis/app/data/models/ProfileResponse;", "getProfile-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "request", "Lcom/healthanalysis/app/data/models/ProfileUpdateRequest;", "updateProfile-gIAlu-s", "(Lcom/healthanalysis/app/data/models/ProfileUpdateRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ProfileRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.healthanalysis.app.data.api.ProfileApi profileApi = null;
    
    @javax.inject.Inject()
    public ProfileRepository(@org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.api.ProfileApi profileApi) {
        super();
    }
}