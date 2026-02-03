package com.kus.domain.login.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)