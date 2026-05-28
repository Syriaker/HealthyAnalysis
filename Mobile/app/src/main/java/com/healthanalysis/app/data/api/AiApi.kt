package com.healthanalysis.app.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class AiGenerateResponse(
    @SerializedName("task_id") val taskId: Int,
    @SerializedName("status") val status: String
)

data class AiStatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("advice_text") val adviceText: String?
)

data class AiHistoryItem(
    @SerializedName(value = "date", alternate = ["created_at", "timestamp"]) val date: String?,
    @SerializedName(value = "text", alternate = ["advice", "advice_text", "content"]) val text: String?
)

interface AiApi {
    @GET("ai/advice/generate/")
    suspend fun generateAdvice(): Response<AiGenerateResponse>

    @GET("ai/advice/status/{taskId}/")
    suspend fun checkStatus(@Path("taskId") taskId: Int): Response<AiStatusResponse>

    @GET("ai/history/")
    suspend fun getHistory(): Response<List<AiHistoryItem>>
}
