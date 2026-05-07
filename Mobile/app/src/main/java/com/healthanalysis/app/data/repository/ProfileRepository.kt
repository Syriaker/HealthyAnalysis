package com.healthanalysis.app.data.repository

import com.healthanalysis.app.data.api.ProfileApi
import com.healthanalysis.app.data.api.httpErrorMessage
import com.healthanalysis.app.data.models.ProfileResponse
import com.healthanalysis.app.data.models.ProfileUpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun getProfile(): Result<ProfileResponse> {
        return try {
            val response = profileApi.getProfile()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(httpErrorMessage(response.code())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(request: ProfileUpdateRequest): Result<ProfileResponse> {
        return try {
            val response = profileApi.patchProfile(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(httpErrorMessage(response.code())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
