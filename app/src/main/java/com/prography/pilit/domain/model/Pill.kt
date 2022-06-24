package com.prography.pilit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pill(
    val alertId:Int,
    val pillName:String,
    val alertTime:List<String>,
    val alertWeek:List<Week>,
    val isPush:Boolean,
    val dosage:Int,
    val eatId:Int,
    val isEaten: Boolean
) : Parcelable