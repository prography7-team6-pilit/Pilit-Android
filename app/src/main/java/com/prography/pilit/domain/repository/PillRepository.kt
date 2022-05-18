package com.prography.pilit.domain.repository

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.data.datasource.remote.pill.EatRequest
import com.prography.pilit.data.datasource.remote.pill.TakeLog
import com.prography.pilit.domain.model.Pill

interface PillRepository {

    suspend fun requestMonthlyPill(year: Int, month: Int): List<TakeLog>

    suspend fun requestAlert(year: Int, month: Int, day: Int): List<Pill>

    suspend fun postTakingLogs(request: EatRequest): Boolean
}
