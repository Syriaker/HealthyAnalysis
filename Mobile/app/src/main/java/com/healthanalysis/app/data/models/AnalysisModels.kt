package com.healthanalysis.app.data.models

import com.google.gson.annotations.SerializedName

data class BiomarkerResponse(
    val id: Int,
    val name: String,
    val unit: String,
    val description: String = ""
)

data class PersonalNormResponse(
    val id: Int,
    val name: String,
    val unit: String,
    @SerializedName("min_value") val minValue: Double,
    @SerializedName("max_value") val maxValue: Double
)

data class AnalysisRecordResponse(
    val id: Int,
    val date: String,
    val laboratory: String = "",
    val comment: String = "",
    val results: List<AnalysisResultResponse> = emptyList()
)

data class AnalysisResultResponse(
    val biomarker: Int,
    @SerializedName("biomarker_name") val biomarkerName: String? = null,
    val value: Double,
    val status: String? = null,
    val unit: String? = null
)

data class LatestAnalysisResponse(
    @SerializedName("biomarker_id") val biomarkerId: Int,
    @SerializedName("biomarker_name") val biomarkerName: String,
    val unit: String,
    val value: Double,
    val status: String,
    val date: String
)

data class AnalysisResultRequest(
    val biomarker: Int,
    val value: Double
)

data class AnalysisRecordRequest(
    val date: String,
    val laboratory: String = "",
    val comment: String = "",
    val results: List<AnalysisResultRequest>
)
