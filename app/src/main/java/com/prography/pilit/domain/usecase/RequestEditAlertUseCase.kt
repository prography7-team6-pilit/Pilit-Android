package com.prography.pilit.domain.usecase

import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.domain.repository.PillRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestEditAlertUseCase @Inject constructor(
    private val pillRepository: PillRepository
){
    suspend operator fun invoke(alertId: Int, body: AddAlertRequest) = pillRepository.requestEditAlert(alertId, body)
}