package com.kus.kustaurant

actual fun getPlatform(): Platform = object : Platform {
    override val name: String = "Native"
}