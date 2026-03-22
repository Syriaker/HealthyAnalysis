package com.healthanalysis.app.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class VerifyRequest(
    val email: String,
    val code: String
)

data class TokenResponse(
    val access: String,
    val refresh: String
)

data class RefreshRequest(
    val refresh: String
)

data class RefreshResponse(
    val access: String
)

data class MessageResponse(
    val message: String? = null,
    val error: String? = null
)
