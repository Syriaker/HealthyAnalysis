package com.healthanalysis.app.data.api

import com.healthanalysis.app.data.models.CustomProductRequest
import com.healthanalysis.app.data.models.CustomProductResponse
import com.healthanalysis.app.data.models.FoodLogRequest
import com.healthanalysis.app.data.models.FoodLogResponse
import com.healthanalysis.app.data.models.ProductResponse
import com.healthanalysis.app.data.models.WaterAmountRequest
import com.healthanalysis.app.data.models.WaterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NutritionApi {

    @GET("nutrition/scan/")
    suspend fun scanProduct(@Query("barcode") barcode: String): Response<ProductResponse>

    @POST("nutrition/products/custom/")
    suspend fun createCustomProduct(@Body request: CustomProductRequest): Response<CustomProductResponse>

    @POST("nutrition/log/")
    suspend fun logFood(@Body request: FoodLogRequest): Response<FoodLogResponse>

    @GET("nutrition/log/")
    suspend fun getFoodLogs(@Query("date") date: String? = null): Response<List<FoodLogResponse>>

    @DELETE("nutrition/log/{id}/")
    suspend fun deleteLog(@Path("id") id: Int): Response<Unit>

    @GET("nutrition/water/")
    suspend fun getWater(@Query("date") date: String? = null): Response<WaterResponse>

    @POST("nutrition/water/")
    suspend fun addWater(@Body request: WaterAmountRequest): Response<WaterResponse>

    @PATCH("nutrition/water/")
    suspend fun setWater(@Body request: WaterAmountRequest): Response<WaterResponse>
}
