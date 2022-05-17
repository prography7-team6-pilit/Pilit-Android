package com.prography.pilit.data.datasource.remote.user

import com.google.gson.annotations.SerializedName

data class UserRequest (

    @SerializedName("uuid")
    val uuid:String,
    @SerializedName("nickname")
    val nickname:String,
    @SerializedName("firebasetoken")
    val firebasetoken:String
)

data class UserResponse(
    @SerializedName("result")
    val result:Boolean,
    @SerializedName("error")
    val error:String?,
    @SerializedName("accessToken")
    val accessToken:String?
)