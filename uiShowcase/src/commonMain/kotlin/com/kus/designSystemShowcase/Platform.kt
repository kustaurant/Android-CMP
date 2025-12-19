package com.kus.designSystemShowcase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform