package com.kus.data.auth.local

interface AuthPreferenceDataSource {
    suspend fun setUserId(value: String)
    suspend fun setAccessToken(value: String)
    suspend fun setRefreshToken(value: String)

    suspend fun getUserId(): String
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String

    suspend fun clear()
}