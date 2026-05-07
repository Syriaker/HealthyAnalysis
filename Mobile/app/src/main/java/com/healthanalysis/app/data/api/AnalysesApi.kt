package com.healthanalysis.app.data.api

import com.healthanalysis.app.data.models.AnalysisRecordRequest
import com.healthanalysis.app.data.models.AnalysisRecordResponse
import com.healthanalysis.app.data.models.LatestAnalysisResponse
import com.healthanalysis.app.data.models.PersonalNormResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AnalysesApi {

    @GET("analyses/norms/")
    suspend fun getPersonalNorms(): Response<List<PersonalNormResponse>>

    @GET("analyses/records/")
    suspend fun getRecords(): Response<List<AnalysisRecordResponse>>

    @POST("analyses/records/")
    suspend fun createRecord(@Body request: AnalysisRecordRequest): Response<AnalysisRecordResponse>

    @GET("analyses/latest/")
    suspend fun getLatest(): Response<List<LatestAnalysisResponse>>
}
