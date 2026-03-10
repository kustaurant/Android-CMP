package com.kus.designsystem.util

fun String.stripHtml(): String =
    this.replace(Regex("<img[^>]*>"), "")
        .replace(Regex("<[^>]+>"), "")
        .replace(Regex("&nbsp;"), " ")
        .replace(Regex("&lt;"), "<")
        .replace(Regex("&gt;"), ">")
        .replace(Regex("&amp;"), "&")
        .replace(Regex("&quot;"), "\"")
        .replace(Regex("\n{3,}"), "\n\n")
        .trim()