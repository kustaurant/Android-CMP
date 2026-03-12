package com.kus.data.auth

import com.kus.data.auth.api.AuthRefreshApi
import com.kus.data.network.auth.TokenManager
import com.kus.data.network.model.RefreshResult
import com.kus.domain.auth.repository.AuthTokenStore 
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenManagerImpl(
    private val store: AuthTokenStore,
    private val refreshApi: AuthRefreshApi,
) : TokenManager {

    private val refreshMutex = Mutex()

    override suspend fun loadAccessToken(): String =
        store.getAccessTokenOrBlank()

    override suspend fun loadRefreshToken(): String =
        store.getRefreshTokenOrBlank()

    override suspend fun refreshAndGetNewAccessToken(): RefreshResult = refreshMutex.withLock {
        val refresh = store.getRefreshTokenOrBlank()
        if (refresh.isBlank()) {
            store.clear()
            return RefreshResult.InvalidRefreshToken
        }

        return try {
            val newTokens = refreshApi.refresh(refresh)
            store.save(newTokens)
            RefreshResult.Success(newTokens.accessToken)
        } catch (e: ResponseException) {
            val code = e.response.status
            if (code == HttpStatusCode.Unauthorized || code == HttpStatusCode.Forbidden) {
                store.clear()
                RefreshResult.InvalidRefreshToken
            } else {
                RefreshResult.NetworkError
            }
        } catch (e: JsonConvertException) {
            store.clear()
            RefreshResult.InvalidRefreshToken
        } catch (e: Throwable) {
            RefreshResult.NetworkError
        }
    }
}
