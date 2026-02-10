package com.kus.data.auth

import com.kus.data.auth.api.AuthRefreshApi
import com.kus.data.network.auth.TokenManager
import com.kus.domain.auth.repository.AuthTokenStore
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenManagerImpl(
    private val store: AuthTokenStore,
    private val refreshApi: AuthRefreshApi,
    private val sessionEvents: SessionEventEmitter,
) : TokenManager {
    private val refreshMutex = Mutex()

    override suspend fun loadAccessToken(): String =
        store.getAccessTokenOrBlank()

    override suspend fun refreshAndGetNewAccessToken(): String = refreshMutex.withLock {
        val refresh = store.getRefreshTokenOrBlank()

        return try {
            val newTokens = refreshApi.refresh(refresh)
            store.save(newTokens)
            newTokens.accessToken
        } catch (e: ClientRequestException) {
            val code = e.response.status
            if (code == io.ktor.http.HttpStatusCode.Unauthorized ||
                code == io.ktor.http.HttpStatusCode.Forbidden
            ) {
                store.clear()
                sessionEvents.emit(SessionEvent.Expired)
            }
            ""
        }
    }
}
