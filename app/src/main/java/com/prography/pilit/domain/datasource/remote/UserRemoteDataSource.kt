package com.prography.pilit.domain.datasource.remote

import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.model.User

interface UserRemoteDataSource {

    suspend fun requestJoin(body: UserRequest): User
}