package com.prography.pilit.domain.repository

import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.model.User

interface UserRepository {

    suspend fun requestJoin(body: UserRequest):User
}