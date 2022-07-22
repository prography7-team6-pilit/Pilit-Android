package com.prography.pilit.data.datasource.remote.user

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserRequest(

    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("firebasetoken")
    val firebasetoken: String
)

@Keep
data class UserResponse(
    @SerializedName("result")
    val result: Boolean,
    @SerializedName("code")
    val error: String?,
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("nickname")
    val nickname: String?
)