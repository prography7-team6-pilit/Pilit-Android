package com.prography.pilit.domain.usecase

import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.repository.PillRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestAlertUseCase @Inject constructor(
    private val pillRepository: PillRepository
) {
    suspend operator fun invoke(year: Int, month: Int, day: Int): List<Pill> =
        pillRepository.requestAlert(year, month, day)
}