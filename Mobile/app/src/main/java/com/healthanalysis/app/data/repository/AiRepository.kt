package com.healthanalysis.app.data.repository

import com.healthanalysis.app.data.api.AiApi
import com.healthanalysis.app.data.api.AiGenerateResponse
import com.healthanalysis.app.data.api.AiHistoryItem
import com.healthanalysis.app.data.api.AiStatusResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(private val api: AiApi) {

    suspend fun generateAdvice(): Result<AiGenerateResponse> = try {
        val response = api.generateAdvice()
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Ошибка запуска анализа: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun checkStatus(taskId: Int): Result<AiStatusResponse> = try {
        val response = api.checkStatus(taskId)
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Ошибка проверки статуса: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getHistory(): Result<List<AiHistoryItem>> = try {
        val response = api.getHistory()
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Ошибка загрузки истории: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
