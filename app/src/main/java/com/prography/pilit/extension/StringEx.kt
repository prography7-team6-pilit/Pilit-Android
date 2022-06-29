package com.prography.pilit.extension

import java.lang.StringBuilder

fun List<String>.toStringWithComma() : String {
    val builder = StringBuilder()
    forEach {
        builder.append("$it, ")
    }
    return builder.toString()
}