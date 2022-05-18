package com.prography.pilit.domain.usecase

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.data.datasource.remote.pill.EatRequest
import com.prography.pilit.data.datasource.remote.pill.TakeLog
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.repository.PillRepository
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestAlertUseCase @Inject constructor(
    private val pillRepository: PillRepository
) {
    suspend operator fun invoke(year: Int, month: Int, day: Int): List<Pill> =
        pillRepository.requestAlert(year, month, day)

}