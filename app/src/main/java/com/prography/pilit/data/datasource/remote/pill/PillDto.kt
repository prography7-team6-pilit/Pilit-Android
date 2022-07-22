package com.prography.pilit.data.datasource.remote.pill

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.prography.pilit.domain.model.Week

@Keep
data class EatResponseMonth(
    @SerializedName("takelogs")
    val takelogs: List<TakeLog>
)

@Keep
data class EatRequest(
    @SerializedName("alertId")
    val alertId: Int
)

@Keep
data class EatResponse(
    @SerializedName("result")
    val result: Boolean
)

@Keep
data class DeleteResponse(
    @SerializedName("result")
    val result: Boolean
)

@Keep
data class AddAlertRequest(
    @SerializedName("alertTime")
    val alertTime: List<String>?,
    @SerializedName("alertWeek")
    val alertWeek: List<Week>?,
    @SerializedName("isPush")
    val isPush: Boolean?,
    @SerializedName("dosage")
    val dosage: Int?,
    @SerializedName("pillName")
    val pillName: String?
)

@Keep
data class AddAlertResponse(
    @SerializedName("result")
    val result: Boolean
)

@Keep
data class AlertResponse(
    @SerializedName("alerts")
    val alerts: List<Alert>
)

@Keep
data class Alert(
    @SerializedName("alertId")
    val alertId: Int,
    @SerializedName("alertTime")
    val alertTime: List<String>,
    @SerializedName("alertWeek")
    val alertWeek: List<Week>,
    @SerializedName("isPush")
    val isPush: Boolean,
    @SerializedName("pillName")
    val pillName: String,
    @SerializedName("dosage")
    val dosage: Int,
    @SerializedName("eatId")
    val eatId: Int,
    @SerializedName("eatResult")
    val eatResult: Boolean
)

@Keep
data class TakeLog(
    @SerializedName("eatDate")
    val eatDate: String,
    @SerializedName("pillState")
    val pillState: Int
)