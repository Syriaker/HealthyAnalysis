package com.healthanalysis.app.data.repository

import com.healthanalysis.app.data.api.AnalysesApi
import com.healthanalysis.app.data.api.httpErrorMessage
import com.healthanalysis.app.data.models.AnalysisRecordRequest
import com.healthanalysis.app.data.models.AnalysisRecordResponse
import com.healthanalysis.app.data.models.LatestAnalysisResponse
import com.healthanalysis.app.data.models.PersonalNormResponse
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalysesRepository @Inject constructor(
    private val api: AnalysesApi
) {
    suspend fun getPersonalNorms(): Result<List<PersonalNormResponse>> = safeCall {
        api.getPersonalNorms()
    }

    suspend fun getRecords(): Result<List<AnalysisRecordResponse>> = safeCall {
        api.getRecords()
    }

    suspend fun getLatest(): Result<List<LatestAnalysisResponse>> = safeCall {
        api.getLatest()
    }

    suspend fun createRecord(request: AnalysisRecordRequest): Result<AnalysisRecordResponse> {
        return try {
            val response = api.createRecord(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Пустой ответ сервера"))
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(parseServerError(response.code(), errorBody)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun <T> safeCall(block: suspend () -> retrofit2.Response<T>): Result<T> {
        return try {
            val response = block()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Пустой ответ сервера"))
            } else {
                Result.failure(Exception(httpErrorMessage(response.code())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseServerError(code: Int, body: String?): String {
        if (body.isNullOrBlank()) return httpErrorMessage(code)
        return try {
            val json = JSONObject(body)
            val keys = json.keys()
            val messages = mutableListOf<String>()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = json.opt(key)
                val text = when (value) {
                    is org.json.JSONArray -> (0 until value.length()).joinToString("; ") { value.getString(it) }
                    else -> value?.toString().orEmpty()
                }
                if (text.isNotBlank()) messages += text
            }
            messages.joinToString("\n").ifBlank { httpErrorMessage(code) }
        } catch (e: Exception) {
            body
        }
    }
}
