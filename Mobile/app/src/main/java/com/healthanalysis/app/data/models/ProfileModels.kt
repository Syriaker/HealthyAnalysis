package com.healthanalysis.app.data.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val email: String,
    @SerializedName("birth_date") val birthDate: String?,
    val gender: String?,
    val height: Int?,
    val weight: Double?,
    val age: Int?
)

data class ProfileUpdateRequest(
    @SerializedName("birth_date") val birthDate: String? = null,
    val gender: String? = null,
    val height: Int? = null,
    val weight: Double? = null
)
