package com.kus.logging

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

actual fun initLogger() {
    Napier.base(
        antilog = object : Antilog() {
            override fun performLog(
                priority: LogLevel,
                tag: String?,
                throwable: Throwable?,
                message: String?
            ) {
                val t = tag ?: "Napier"
                val m = message ?: ""
                println("[$t] $m")
                throwable?.printStackTrace()
            }
        }
    )
}