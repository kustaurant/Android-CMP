package com.kus.data.auth

actual fun randomUUID(): String = platform.Foundation.NSUUID().UUIDString()