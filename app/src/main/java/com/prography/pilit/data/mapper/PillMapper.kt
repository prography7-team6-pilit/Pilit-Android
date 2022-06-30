package com.prography.pilit.data.mapper

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.domain.model.Pill
import java.lang.StringBuilder

fun Alert.toPill(): Pill {
    return Pill(
        alertId = alertId,
        pillName = pillName,
        alertTime = alertTime,
        alertWeek = alertWeek,
        isPush = isPush,
        dosage = dosage,
        eatId = eatId,
        isEaten = eatResult
    )
}

