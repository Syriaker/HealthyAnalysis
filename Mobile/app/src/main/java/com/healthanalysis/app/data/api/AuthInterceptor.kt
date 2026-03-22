package com.healthanalysis.app.data.api

import com.healthanalysis.app.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        if (url.contains("auth/login") || url.contains("auth/register") ||
            url.contains("auth/verify") || url.contains("auth/token/refresh")
        ) {
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking { tokenManager.getAccessTokenSync() }

        val request = if (accessToken != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()
            val refreshToken = runBlocking { tokenManager.getRefreshTokenSync() } ?: return response

            val refreshRequest = originalRequest.newBuilder()
                .url("${originalRequest.url.scheme}://${originalRequest.url.host}:${originalRequest.url.port}/api/auth/token/refresh/")
                .post(
                    """{"refresh":"$refreshToken"}"""
                        .toRequestBody("application/json".toMediaType())
                )
                .build()

            val refreshResponse = chain.proceed(refreshRequest)

            if (refreshResponse.isSuccessful) {
                val body = refreshResponse.body?.string()
                val newAccessToken = body?.let {
                    try {
                        JSONObject(it).getString("access")
                    } catch (e: Exception) {
                        null
                    }
                }

                refreshResponse.close()

                val token = newAccessToken ?: return chain.proceed(originalRequest)
                runBlocking { tokenManager.updateAccessToken(token) }

                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(newRequest)
            } else {
                refreshResponse.close()
                runBlocking { tokenManager.clearTokens() }
            }
        }

        return response
    }
}
