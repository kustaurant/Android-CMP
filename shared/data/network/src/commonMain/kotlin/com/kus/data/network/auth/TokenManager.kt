package com.kus.data.network.auth

import com.kus.data.network.model.RefreshResult

interface TokenManager {
    suspend fun loadAccessToken(): String

    suspend fun loadRefreshToken(): String

    suspend fun refreshAndGetNewAccessToken(): RefreshResult
}

