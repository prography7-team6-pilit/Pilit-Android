package com.prography.pilit.domain.usecase

import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestJoinUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(body: UserRequest) = userRepository.requestJoin(body)
}
