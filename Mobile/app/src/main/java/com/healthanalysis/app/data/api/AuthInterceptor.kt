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

        if (response.code != 401) {
            return response
        }

        val refreshToken = runBlocking { tokenManager.getRefreshTokenSync() }
            ?: return response

        response.close()

        val refreshUrl = originalRequest.url.newBuilder()
            .encodedPath("/api/auth/token/refresh/")
            .build()
        val refreshRequest = originalRequest.newBuilder()
            .url(refreshUrl)
            .post(
                """{"refresh":"$refreshToken"}"""
                    .toRequestBody("application/json".toMediaType())
            )
            .build()

        val refreshResponse = chain.proceed(refreshRequest)

        if (!refreshResponse.isSuccessful) {
            refreshResponse.close()
            runBlocking { tokenManager.clearTokens() }
            return chain.proceed(request)
        }

        val body = refreshResponse.body?.string()
        refreshResponse.close()
        val newAccessToken = body?.let {
            try {
                JSONObject(it).getString("access")
            } catch (e: Exception) {
                null
            }
        } ?: return chain.proceed(request)

        runBlocking { tokenManager.updateAccessToken(newAccessToken) }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
        return chain.proceed(newRequest)
    }
}
