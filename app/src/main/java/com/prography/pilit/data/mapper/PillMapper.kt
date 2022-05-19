package com.prography.pilit.data.mapper

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.domain.model.Pill
import java.text.SimpleDateFormat

fun Alert.toPill(): Pill {
    val hour24 = alertTime.substring(0,2).toInt()
    var hour12 = hour24
    var am_pm = false

    // 12시간 시스템 형태로 변환
    if(hour24 >= 12) {
        am_pm = true
        hour12 %= 12
    }

    return Pill(
        alertId = alertId,
        pillName = pillName,
        am_pm = am_pm, // 오전 false, 오후 true
        alertTime = alertTime,
        alertWeek = alertWeek,
        isPush = isPush,
        eatId = eatId,
        isEaten = eatResult
    )
}