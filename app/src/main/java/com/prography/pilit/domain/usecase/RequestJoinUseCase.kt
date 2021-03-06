package com.prography.pilit.domain.usecase

import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.model.User
import com.prography.pilit.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestJoinUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(body: UserRequest): Resource<User> = withContext(Dispatchers.IO) {
        userRepository.requestJoin(body)
    }
}
