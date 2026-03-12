package com.kus.data.auth

actual fun randomUUID(): String = java.util.UUID.randomUUID().toString()