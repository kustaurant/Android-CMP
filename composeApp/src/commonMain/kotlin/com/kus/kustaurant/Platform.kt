package com.kus.kustaurant

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform