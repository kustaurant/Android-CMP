package com.kus.data.auth.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
)
