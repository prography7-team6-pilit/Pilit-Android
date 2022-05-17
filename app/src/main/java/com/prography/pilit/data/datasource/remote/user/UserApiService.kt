package com.prography.pilit.data.datasource.remote.user

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/api/v1/users/join")
    fun requestJoin(@Body body: UserRequest): Call<UserResponse>
}