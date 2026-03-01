package com.kus.data.network.auth

interface TokenManager {
    suspend fun loadAccessToken(): String

    suspend fun refreshAndGetNewAccessToken(): String

    suspend fun loadRefreshToken(): String  
}

