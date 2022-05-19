package com.prography.pilit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pill(
    val alertId:Int,
    val pillName:String,
    val am_pm:Boolean, // 오전 false, 오후 true
    val alertTime:String,
    val alertWeek:List<Week>,
    val isPush:Boolean,
    val eatId:Int,
    val isEaten: Boolean
) : Parcelable