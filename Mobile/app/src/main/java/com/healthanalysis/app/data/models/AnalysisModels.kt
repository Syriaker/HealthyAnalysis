package com.healthanalysis.app.data.models

import com.google.gson.annotations.SerializedName

data class BiomarkerResponse(
    val id: Int,
    val name: String,
    val unit: String,
    val description: String
)

data class ReferenceRangeResponse(
    val id: Int,
    val biomarker: Int,
    val gender: String,
    @SerializedName("min_age") val minAge: Int,
    @SerializedName("max_age") val maxAge: Int,
    @SerializedName("min_value") val minValue: Double,
    @SerializedName("max_value") val maxValue: Double
)

data class AnalysisRecordResponse(
    val id: Int,
    val date: String,
    val laboratory: String,
    val comment: String,
    @SerializedName("created_at") val createdAt: String,
    val results: List<AnalysisResultResponse>?
)

data class AnalysisResultResponse(
    val id: Int,
    val biomarker: BiomarkerResponse?,
    val value: Double,
    val status: String
)
