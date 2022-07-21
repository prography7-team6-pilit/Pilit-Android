package com.prography.pilit.data.datasource.remote.user

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {

    @POST("/api/v1/users/join")
    suspend fun requestJoin(@Body body: UserRequest): UserResponse

    @GET("/api/v1/users/login")
    suspend fun login(@Query("uuid") uuid: String): UserResponse
}