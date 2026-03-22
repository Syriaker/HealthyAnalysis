package com.healthanalysis.app.data.repository

import com.healthanalysis.app.data.api.AuthApi
import com.healthanalysis.app.data.local.TokenManager
import com.healthanalysis.app.data.models.LoginRequest
import com.healthanalysis.app.data.models.TokenResponse
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

    suspend fun logout() {
        tokenManager.clearTokens()
    }
}
