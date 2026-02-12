package com.kus.logging

import io.github.aakira.napier.Napier

object KusLog {
    fun d(tag: String = "App", msg: String) = Napier.d(msg, tag = tag)
    fun e(tag: String = "App", msg: String, t: Throwable? = null) = Napier.e(msg, t, tag)
}