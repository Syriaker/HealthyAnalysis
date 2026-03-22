package com.healthanalysis.app.data.api

import com.healthanalysis.app.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<MessageResponse>

    @POST("auth/verify/")
    suspend fun verify(@Body request: VerifyRequest): Response<MessageResponse>

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<RefreshResponse>
}
