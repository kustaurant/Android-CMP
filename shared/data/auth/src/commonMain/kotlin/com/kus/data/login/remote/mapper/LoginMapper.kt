package com.kus.data.login.remote.mapper

import com.kus.data.login.remote.response.LoginResponse
import com.kus.domain.login.model.AuthToken

fun LoginResponse.toDomain() = AuthToken(
    accessToken = accessToken,
    refreshToken = refreshToken
)