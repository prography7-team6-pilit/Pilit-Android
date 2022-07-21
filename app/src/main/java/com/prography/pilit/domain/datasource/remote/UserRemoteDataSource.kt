package com.prography.pilit.domain.datasource.remote

import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.model.User

interface UserRemoteDataSource {

    suspend fun requestJoin(body: UserRequest): Resource<User>

    suspend fun login(uuid: String): Resource<User>
}