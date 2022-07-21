package com.prography.pilit.data.mapper

import com.prography.pilit.data.datasource.remote.user.UserResponse
import com.prography.pilit.domain.model.User

fun UserResponse.toUser(): User =
    if (result && error == null) {
        User(accessToken = accessToken, nickname = nickname)
    } else {
        User(errorCode = error, accessToken = null, nickname = null)
    }