package com.prography.pilit.domain.usecase

import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.domain.model.User
import com.prography.pilit.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * {description}
 *
 * @author capri
 * @since 2022/07/21
 */
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(uuid: String): Resource<User> = withContext(Dispatchers.IO) {
        userRepository.login(uuid)
    }
}