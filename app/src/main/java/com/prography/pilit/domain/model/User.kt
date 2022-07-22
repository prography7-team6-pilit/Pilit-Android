package com.prography.pilit.domain.model

import androidx.annotation.Keep

@Keep
data class User(
    val errorCode: String? = null,
    val accessToken: String?,
    val nickname: String?
)