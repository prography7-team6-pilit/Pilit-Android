package com.prography.pilit.data.repository

import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.datasource.remote.UserRemoteDataSource
import com.prography.pilit.domain.model.User
import com.prography.pilit.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun requestJoin(body: UserRequest): Resource<User> =
        remoteDataSource.requestJoin(body)

    override suspend fun login(uuid: String): Resource<User> =
        remoteDataSource.login(uuid)

}