package com.prography.pilit.extension

import java.lang.StringBuilder

fun List<String>.toStringWithComma() : String {
    val builder = StringBuilder()
    forEachIndexed { index, s ->
        val data = s.toFormattedString()
        if (index == this.lastIndex) {
            builder.append(data)
        } else {
            builder.append("$data, ")
        }
    }
    return builder.toString()
}