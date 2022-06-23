package com.prography.pilit.data.mapper

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.domain.model.Pill

fun Alert.toPill(): Pill {
    return Pill(
        alertId = alertId,
        pillName = pillName,
        alertTime = alertTime,
        alertWeek = alertWeek,
        isPush = isPush,
        eatId = eatId,
        isEaten = eatResult
    )
}