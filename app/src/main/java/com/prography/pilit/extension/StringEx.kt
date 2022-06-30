package com.prography.pilit.extension

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toFormattedString(): String {
    return try {
        val format = SimpleDateFormat("hh:mm", Locale.KOREA)
        val formatter = SimpleDateFormat("a hh:mm", Locale.KOREA)
        formatter.format(format.parse(this) ?: throw Exception())
    } catch (e: Exception) {
        ""
    }
}