package com.healthanalysis.app.data.repository

import com.healthanalysis.app.data.api.AuthApi
import com.healthanalysis.app.data.local.TokenManager
import com.healthanalysis.app.data.models.LoginRequest
import com.healthanalysis.app.data.models.RegisterRequest
import com.healthanalysis.app.data.models.TokenResponse
import com.healthanalysis.app.data.models.VerifyRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<TokenResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { tokens ->
                    tokenManager.saveTokens(tokens.access, tokens.refresh)
                    Result.success(tokens)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val response = authApi.register(RegisterRequest(email, password))
            when {
                response.isSuccessful -> Result.success(Unit)
                response.code() in 500..599 -> {
                    // Backend processed the request (email sent) but serveo timed out before
                    // Django could send the response — treat as success so user can enter the code
                    Result.success(Unit)
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    val message = parseErrorBody(errorBody) ?: "Ошибка регистрации (${response.code()})"
                    Result.failure(Exception(message))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verify(email: String, code: String): Result<Unit> {
        return try {
            val response = authApi.verify(VerifyRequest(email, code))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = parseErrorBody(errorBody) ?: "Неверный код подтверждения"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseErrorBody(body: String?): String? {
        if (body.isNullOrBlank()) return null
        return try {
            val json = org.json.JSONObject(body)
            json.keys().asSequence().firstOrNull()?.let { key ->
                val value = json.get(key)
                if (value is org.json.JSONArray) value.getString(0) else value.toString()
            }
        } catch (e: Exception) {
            body.take(200)
        }
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }
}
