package com.prography.pilit.data.datasource.remote.user

import com.prography.pilit.data.mapper.toUser
import com.prography.pilit.domain.datasource.remote.UserRemoteDataSource
import com.prography.pilit.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRemoteDataSource {

    override suspend fun requestJoin(body:UserRequest): User = withContext(Dispatchers.IO) {
        userApiService.requestJoin(body).await().toUser()
    }
}