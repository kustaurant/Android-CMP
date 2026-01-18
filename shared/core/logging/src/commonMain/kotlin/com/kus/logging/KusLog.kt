package com.kus.logging

import io.github.aakira.napier.Napier

object KusLog {
    fun d(msg: String, tag: String = "App") = Napier.d(msg, tag = tag)
    fun e(msg: String, t: Throwable? = null, tag: String = "App") = Napier.e(msg, t, tag)
}