package com.prography.pilit.data.repository

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.data.datasource.remote.pill.EatRequest
import com.prography.pilit.data.datasource.remote.pill.PillApiService
import com.prography.pilit.data.datasource.remote.pill.TakeLog
import com.prography.pilit.data.mapper.toPill
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.repository.PillRepository
import javax.inject.Inject

class PillRepositoryImpl @Inject constructor(
    private val pillApiService: PillApiService
) : PillRepository{
    override suspend fun requestMonthlyPill(year: Int, month: Int): List<TakeLog> =
        pillApiService.requestMonthlyPill(year, month).takelogs

    override suspend fun requestAlert(year: Int, month: Int, day: Int): List<Pill> =
        pillApiService.requestAlert(year, month, day).alerts.map { it.toPill() }

    override suspend fun requestTakingLogs(request: EatRequest): Boolean =
        pillApiService.requestTakingLogs(request).result

}