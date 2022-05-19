package com.prography.pilit.data.datasource.remote.pill

import com.google.gson.annotations.SerializedName
import com.prography.pilit.domain.model.Week

data class EatResponseMonth(
    @SerializedName("takelogs")
    val takelogs: List<TakeLog>
)

data class EatRequest(
    @SerializedName("alertId")
    val alertId: Int
)

data class EatResponse(
    @SerializedName("result")
    val result: Boolean
)

data class AlertResponse(
    @SerializedName("alerts")
    val alerts: List<Alert>
)

data class Alert(
    @SerializedName("alertId")
    val alertId: Int,
    @SerializedName("alertTime")
    val alertTime: String,
    @SerializedName("alertWeek")
    val alertWeek: List<Week>,
    @SerializedName("isPush")
    val isPush: Boolean,
    @SerializedName("pillName")
    val pillName: String,
    @SerializedName("eatId")
    val eatId: Int,
    @SerializedName("eatResult")
    val eatResult: Boolean
)

data class TakeLog(
    @SerializedName("eatDate")
    val eatDate: String,
    @SerializedName("pillState")
    val pillState: Int
)