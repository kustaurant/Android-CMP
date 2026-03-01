package com.kus.feature.community.ui.util

import kotlin.text.iterator

fun String.jsQuote(): String {
    return buildString {
        append('"')
        for (c in this@jsQuote) {
            when (c) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(c)
            }
        }
        append('"')
    }
}