package com.prography.pilit.data.repository

import com.prography.pilit.data.datasource.remote.pill.*
import com.prography.pilit.data.mapper.toPill
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.repository.PillRepository
import retrofit2.Response
import javax.inject.Inject

class PillRepositoryImpl @Inject constructor(
    private val pillApiService: PillApiService
) : PillRepository {
    override suspend fun requestMonthlyPill(year: Int, month: Int): List<TakeLog> =
        pillApiService.requestMonthlyPill(year, month).takelogs

    override suspend fun requestAlert(year: Int, month: Int, day: Int): List<Pill> =
        pillApiService.requestAlert(year, month, day).alerts.map { it.toPill() }

    override suspend fun postTakingLogs(request: EatRequest): Boolean =
        pillApiService.postTakingLogs(request).result

    override suspend fun requestEditAlert(alertId: Int, body:AddAlertRequest): Response<AddAlertResponse> =
        pillApiService.requestEditAlert(alertId, body)

    override suspend fun requestDeleteAlert(alertId: Int): Response<DeleteResponse> =
        pillApiService.requestDeleteAlert(alertId)
}