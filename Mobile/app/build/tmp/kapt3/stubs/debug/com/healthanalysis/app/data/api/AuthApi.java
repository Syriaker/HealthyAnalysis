package com.healthanalysis.app.data.api;

import com.healthanalysis.app.data.models.*;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/healthanalysis/app/data/api/AuthApi;", "", "login", "Lretrofit2/Response;", "Lcom/healthanalysis/app/data/models/TokenResponse;", "request", "Lcom/healthanalysis/app/data/models/LoginRequest;", "(Lcom/healthanalysis/app/data/models/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshToken", "Lcom/healthanalysis/app/data/models/RefreshResponse;", "Lcom/healthanalysis/app/data/models/RefreshRequest;", "(Lcom/healthanalysis/app/data/models/RefreshRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "Lcom/healthanalysis/app/data/models/MessageResponse;", "Lcom/healthanalysis/app/data/models/RegisterRequest;", "(Lcom/healthanalysis/app/data/models/RegisterRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verify", "Lcom/healthanalysis/app/data/models/VerifyRequest;", "(Lcom/healthanalysis/app/data/models/VerifyRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface AuthApi {
    
    @retrofit2.http.POST(value = "auth/register/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object register(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.RegisterRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.MessageResponse>> $completion);
    
    @retrofit2.http.POST(value = "auth/verify/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object verify(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.VerifyRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.MessageResponse>> $completion);
    
    @retrofit2.http.POST(value = "auth/login/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.LoginRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.TokenResponse>> $completion);
    
    @retrofit2.http.POST(value = "auth/token/refresh/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object refreshToken(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.RefreshRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.RefreshResponse>> $completion);
}