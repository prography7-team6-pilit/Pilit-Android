package com.prography.pilit.domain.usecase

import com.prography.pilit.domain.repository.PillRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestDeleteAlertUseCase @Inject constructor(
    private val pillRepository: PillRepository
){
    suspend operator fun invoke(alertId: Int) = pillRepository.requestDeleteAlert(alertId)
}