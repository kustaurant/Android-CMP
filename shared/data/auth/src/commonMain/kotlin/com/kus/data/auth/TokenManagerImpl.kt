package com.kus.data.auth

import com.kus.data.auth.api.AuthRefreshApi
import com.kus.data.auth.local.AuthPreferenceDataSource
import com.kus.data.auth.model.AuthTokens
import com.kus.data.network.auth.TokenManager
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenManagerImpl(
    private val prefs: AuthPreferenceDataSource,
    private val refreshApi: AuthRefreshApi,
    private val sessionEvents: SessionEventEmitter,
) : TokenManager {
    private val refreshMutex = Mutex()

    suspend fun loadTokensOrNull(): AuthTokens? {
        val access = prefs.getAccessToken().trim()
        val refresh = prefs.getRefreshToken().trim()
        if (access.isEmpty() || refresh.isEmpty()) return null
        return AuthTokens(accessToken = access, refreshToken = refresh)
    }

    suspend fun saveTokens(tokens: AuthTokens) {
        prefs.setAccessToken(tokens.accessToken)
        prefs.setRefreshToken(tokens.refreshToken)
    }

    suspend fun clear() {
        prefs.clear()
    }

    suspend fun refreshTokensOrNull(): AuthTokens? = refreshMutex.withLock {
        val refreshHeaderValue = prefs.getRefreshToken().trim()
        if (refreshHeaderValue.isEmpty()) return null

        return runCatching {
            val newTokens = refreshApi.refresh(refreshHeaderValue)
            saveTokens(newTokens)
            newTokens
        }.getOrNull()
    }

    override suspend fun loadAccessToken(): String? {
        val access = prefs.getAccessToken().trim()
        return access.ifEmpty { null }
    }

    override suspend fun refreshAndGetNewAccessToken(): String? = refreshMutex.withLock {
        val refresh = prefs.getRefreshToken().trim()
        if (refresh.isEmpty()) return null

        return try {
            val newTokens = refreshApi.refresh(refresh)
            prefs.setAccessToken(newTokens.accessToken)
            prefs.setRefreshToken(newTokens.refreshToken)
            newTokens.accessToken
        } catch (e: ClientRequestException) {
            val code = e.response.status
            if (code == io.ktor.http.HttpStatusCode.Unauthorized ||
                code == io.ktor.http.HttpStatusCode.Forbidden
            ) {
                prefs.clear()
                sessionEvents.emit(SessionEvent.Expired)
            }
            null
        }
    }
}
