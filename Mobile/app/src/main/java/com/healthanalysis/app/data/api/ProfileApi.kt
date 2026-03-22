package com.healthanalysis.app.data.api

import com.healthanalysis.app.data.models.ProfileResponse
import com.healthanalysis.app.data.models.ProfileUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface ProfileApi {

    @GET("profile/me/")
    suspend fun getProfile(): Response<ProfileResponse>

    @PUT("profile/me/")
    suspend fun updateProfile(@Body request: ProfileUpdateRequest): Response<ProfileResponse>

    @PATCH("profile/me/")
    suspend fun patchProfile(@Body request: ProfileUpdateRequest): Response<ProfileResponse>
}
