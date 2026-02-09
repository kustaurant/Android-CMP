package com.kus.data.auth.remote.mapper

import com.kus.data.auth.remote.response.LoginResponse
import com.kus.domain.auth.model.AuthToken

fun LoginResponse.toDomain() = AuthToken(
    accessToken = accessToken,
    refreshToken = refreshToken
)