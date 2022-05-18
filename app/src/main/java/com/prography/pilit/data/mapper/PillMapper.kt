package com.prography.pilit.data.mapper

import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.domain.model.Pill

fun Alert.toPill(): Pill =
    Pill(pillName, alertTime, alertId, eatResult)