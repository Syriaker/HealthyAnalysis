package com.healthanalysis.app.data.api;

import com.healthanalysis.app.data.models.ProfileResponse;
import com.healthanalysis.app.data.models.ProfileUpdateRequest;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u000b"}, d2 = {"Lcom/healthanalysis/app/data/api/ProfileApi;", "", "getProfile", "Lretrofit2/Response;", "Lcom/healthanalysis/app/data/models/ProfileResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "patchProfile", "request", "Lcom/healthanalysis/app/data/models/ProfileUpdateRequest;", "(Lcom/healthanalysis/app/data/models/ProfileUpdateRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "app_debug"})
public abstract interface ProfileApi {
    
    @retrofit2.http.GET(value = "profile/me/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.ProfileResponse>> $completion);
    
    @retrofit2.http.PUT(value = "profile/me/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateProfile(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.ProfileUpdateRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.ProfileResponse>> $completion);
    
    @retrofit2.http.PATCH(value = "profile/me/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object patchProfile(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.ProfileUpdateRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.ProfileResponse>> $completion);
}