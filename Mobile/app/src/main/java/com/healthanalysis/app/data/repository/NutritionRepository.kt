package com.healthanalysis.app.data.repository

import com.healthanalysis.app.data.api.NutritionApi
import com.healthanalysis.app.data.models.CustomProductRequest
import com.healthanalysis.app.data.models.CustomProductResponse
import com.healthanalysis.app.data.models.FoodLogRequest
import com.healthanalysis.app.data.models.FoodLogResponse
import com.healthanalysis.app.data.models.ProductResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NutritionRepository @Inject constructor(
    private val nutritionApi: NutritionApi
) {
    suspend fun scanProduct(barcode: String): Result<ProductResponse> {
        return try {
            val response = nutritionApi.scanProduct(barcode)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Product not found"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCustomProduct(request: CustomProductRequest): Result<CustomProductResponse> {
        return try {
            val response = nutritionApi.createCustomProduct(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Пустой ответ сервера"))
            } else {
                Result.failure(Exception("Ошибка ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logFood(request: FoodLogRequest): Result<FoodLogResponse> {
        return try {
            val response = nutritionApi.logFood(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFoodLogs(date: String? = null): Result<List<FoodLogResponse>> {
        return try {
            val response = nutritionApi.getFoodLogs(date)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
