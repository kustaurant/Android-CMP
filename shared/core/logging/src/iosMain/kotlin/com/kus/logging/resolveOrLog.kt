package com.kus.logging

import kotlin.reflect.KClass
import org.koin.core.Koin

fun resolveOrLog(koin: Koin, clazz: KClass<*>) {
    runCatching { koin.get<Any>(clazz) }
        .onSuccess { println("✅ resolved: ${clazz.simpleName} => $it") }
        .onFailure { e ->
            println("❌ resolve failed: ${clazz.simpleName} => ${e::class.simpleName}: ${e.message}")
            var c: Throwable? = e
            while (c != null) {
                println("   cause => ${c::class.simpleName}: ${c.message}")
                c = c.cause
            }
        }
}