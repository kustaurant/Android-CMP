package com.kus.data.auth.repository

import com.kus.data.auth.local.AuthPreferenceDataSource
import com.kus.domain.auth.model.AuthToken
import com.kus.domain.auth.repository.AuthTokenStore

class AuthTokenStoreImpl(
    private val prefs: AuthPreferenceDataSource
) : AuthTokenStore {
    override suspend fun getAccessTokenOrBlank(): String {
        return prefs.getAccessToken()
    }

    override suspend fun getRefreshTokenOrBlank(): String {
        return prefs.getRefreshToken()
    }

    override suspend fun save(tokens: AuthToken) {
        prefs.setAccessToken(tokens.accessToken)
        tokens.refreshToken?.let {
            prefs.setRefreshToken(it)
        }
    }

    override suspend fun clear() {
        prefs.clear()
    }
}