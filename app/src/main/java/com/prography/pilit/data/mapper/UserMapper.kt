package com.prography.pilit.data.mapper

import com.prography.pilit.data.datasource.remote.user.UserResponse
import com.prography.pilit.domain.model.User

fun UserResponse.toUser(): User =
    User(
        accessToken
    )