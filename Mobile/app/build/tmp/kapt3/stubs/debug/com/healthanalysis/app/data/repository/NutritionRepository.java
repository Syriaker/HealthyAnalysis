package com.healthanalysis.app.data.repository;

import com.healthanalysis.app.data.api.NutritionApi;
import com.healthanalysis.app.data.models.FoodLogRequest;
import com.healthanalysis.app.data.models.FoodLogResponse;
import com.healthanalysis.app.data.models.ProductResponse;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\t\u0010\nJ$\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000e\u0010\u000fJ$\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u00062\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0016"}, d2 = {"Lcom/healthanalysis/app/data/repository/NutritionRepository;", "", "nutritionApi", "Lcom/healthanalysis/app/data/api/NutritionApi;", "(Lcom/healthanalysis/app/data/api/NutritionApi;)V", "getFoodLogs", "Lkotlin/Result;", "", "Lcom/healthanalysis/app/data/models/FoodLogResponse;", "getFoodLogs-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logFood", "request", "Lcom/healthanalysis/app/data/models/FoodLogRequest;", "logFood-gIAlu-s", "(Lcom/healthanalysis/app/data/models/FoodLogRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scanProduct", "Lcom/healthanalysis/app/data/models/ProductResponse;", "barcode", "", "scanProduct-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class NutritionRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.healthanalysis.app.data.api.NutritionApi nutritionApi = null;
    
    @javax.inject.Inject()
    public NutritionRepository(@org.jetbrains.annotations.NotNull()
    com.healthanalysis.app.data.api.NutritionApi nutritionApi) {
        super();
    }
}