package com.healthanalysis.app.data.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        var lastException: IOException? = null
        var response: Response? = null

        while (attempt < MAX_ATTEMPTS) {
            try {
                response?.close()
                response = chain.proceed(request)
                if (response.code !in RETRYABLE_CODES) {
                    return response
                }
            } catch (e: IOException) {
                lastException = e
            }

            attempt++
            if (attempt < MAX_ATTEMPTS) {
                try {
                    Thread.sleep(BACKOFF_MS * attempt)
                } catch (_: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }

        return response ?: throw (lastException ?: IOException("Network request failed"))
    }

    companion object {
        private const val MAX_ATTEMPTS = 5
        private const val BACKOFF_MS = 1500L
        private val RETRYABLE_CODES = setOf(502, 503, 504)
    }
}
