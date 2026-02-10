package com.kus.domain.auth.repository

import com.kus.domain.auth.model.AuthToken

interface AuthTokenStore {
    suspend fun getAccessTokenOrBlank(): String
    suspend fun getRefreshTokenOrBlank(): String
    suspend fun save(tokens: AuthToken)
    suspend fun clear()
}
