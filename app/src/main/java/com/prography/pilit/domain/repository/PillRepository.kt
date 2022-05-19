package com.prography.pilit.domain.repository

import com.prography.pilit.data.datasource.remote.pill.*
import com.prography.pilit.domain.model.Pill
import retrofit2.Response

interface PillRepository {

    suspend fun requestMonthlyPill(year: Int, month: Int): List<TakeLog>

    suspend fun requestAlert(year: Int, month: Int, day: Int): List<Pill>

    suspend fun postTakingLogs(request: EatRequest): Boolean

    suspend fun requestEditAlert(alertId: Int, body: AddAlertRequest): Response<AddAlertResponse>

    suspend fun requestDeleteAlert(alertId: Int): Response<DeleteResponse>
}
