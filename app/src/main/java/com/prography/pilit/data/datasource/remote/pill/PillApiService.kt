package com.prography.pilit.data.datasource.remote.pill
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface PillApiService {

    @GET("/api/v1/pills/monthly")
    suspend fun requestMonthlyPill(@Query("year") year: Int, @Query("month") month: Int): EatResponseMonth

    @GET("/api/v1/pills/taking-logs")
    suspend fun requestTakingLogs(@Body body: EatRequest): EatResponse

    @GET("/api/v1/pill-alerts")
    suspend fun requestAlert(@Query("year") year: Int, @Query("month") month: Int, @Query("day") day: Int): AlertResponse
}