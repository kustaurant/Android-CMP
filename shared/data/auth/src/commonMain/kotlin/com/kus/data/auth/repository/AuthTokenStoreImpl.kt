package com.kus.data.auth.repository

import com.kus.data.auth.local.AuthPreferenceDataSource
import com.kus.domain.auth.model.AuthToken
import com.kus.domain.auth.repository.AuthTokenStore

class AuthTokenStoreImpl(
    private val prefs: AuthPreferenceDataSource
) : AuthTokenStore {

    override suspend fun save(tokens: AuthToken) {
        prefs.setAccessToken(tokens.accessToken)
        prefs.setRefreshToken(tokens.refreshToken)
    }

    override suspend fun clear() {
        prefs.clear()
    }
}