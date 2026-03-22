package com.healthanalysis.app.data.api;

import com.healthanalysis.app.data.models.FoodLogRequest;
import com.healthanalysis.app.data.models.FoodLogResponse;
import com.healthanalysis.app.data.models.ProductResponse;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u00032\b\b\u0001\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00032\b\b\u0001\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/healthanalysis/app/data/api/NutritionApi;", "", "getFoodLogs", "Lretrofit2/Response;", "", "Lcom/healthanalysis/app/data/models/FoodLogResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logFood", "request", "Lcom/healthanalysis/app/data/models/FoodLogRequest;", "(Lcom/healthanalysis/app/data/models/FoodLogRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scanProduct", "Lcom/healthanalysis/app/data/models/ProductResponse;", "barcode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface NutritionApi {
    
    @retrofit2.http.GET(value = "nutrition/scan/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object scanProduct(@retrofit2.http.Query(value = "barcode")
    @org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.ProductResponse>> $completion);
    
    @retrofit2.http.POST(value = "nutrition/log/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object logFood(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.models.FoodLogRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.healthanalysis.app.data.models.FoodLogResponse>> $completion);
    
    @retrofit2.http.GET(value = "nutrition/log/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFoodLogs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.healthanalysis.app.data.models.FoodLogResponse>>> $completion);
}