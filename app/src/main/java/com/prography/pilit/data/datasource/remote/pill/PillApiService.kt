package com.prography.pilit.data.datasource.remote.pill
import retrofit2.http.*

interface PillApiService {

    @GET("/api/v1/pills/monthly")
    suspend fun requestMonthlyPill(@Query("year") year: Int, @Query("month") month: Int): EatResponseMonth

    @POST("/api/v1/pills/taking-logs")
    suspend fun postTakingLogs(@Body body: EatRequest): EatResponse

    @GET("/api/v1/pill-alerts")
    suspend fun requestAlert(@Query("year") year: Int, @Query("month") month: Int, @Query("day") day: Int): AlertResponse

    @POST("/api/v1/pill-alerts")
    suspend fun requestAddAlert(@Body body:AddAlertRequest): AddAlertResponse

    @PUT("/api/v1/pill-alerts/{alertId}")
    suspend fun requestEditAlert(@Path("alertId") alertId: Int, @Body body:AddAlertRequest): AddAlertResponse

    @DELETE("/api/v1/pill-alerts/{alertId}")
    suspend fun requestDeleteAlert(@Path("alertId") alertId: Int): DeleteResponse
}