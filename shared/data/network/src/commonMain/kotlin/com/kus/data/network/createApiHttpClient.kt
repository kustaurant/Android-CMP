package com.kus.data.network

import com.kus.data.network.auth.TokenManager
import com.kus.data.network.model.RefreshResult
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.plugin
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createApiHttpClient(
    engine: HttpClientEngine,
    tokenManager: TokenManager,
    isDebug: Boolean = true,
    baseUrl: String,
    sessionEvents: SessionEventEmitter,
    additionalConfig: HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    val client = HttpClient(engine = engine) {

        defaultRequest { url.takeFrom(baseUrl) }
        expectSuccess = false

        if (isDebug) {
            install(Logging) { logger = Logger.SIMPLE; level = LogLevel.ALL }
        }

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; explicitNulls = false; isLenient = true })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 15_000
        }

        install(Auth) {
            bearer {
                sendWithoutRequest { request ->
                    val path = request.url.encodedPath
                    val isPublic =
                        path.startsWith("/api/v2/login/naver") ||
                                path.startsWith("/api/v2/token/refresh")
                    !isPublic
                }

                loadTokens {
                    val access = tokenManager.loadAccessToken()
                    val refresh = tokenManager.loadRefreshToken()
                    if (access.isNotBlank() && refresh.isNotBlank()) {
                        BearerTokens(accessToken = access, refreshToken = refresh)
                    } else null
                }

                refreshTokens {
                    val refresh = tokenManager.loadRefreshToken()

                    if (refresh.isBlank()) return@refreshTokens null

                    when (val result = tokenManager.refreshAndGetNewAccessToken()) {
                        is RefreshResult.Success -> {
                            BearerTokens(accessToken = result.accessToken, refreshToken = refresh)
                        }

                        is RefreshResult.InvalidRefreshToken -> {
                            sessionEvents.emit(SessionEvent.Expired)
                            null
                        }

                        is RefreshResult.NetworkError -> null
                    }
                }
            }
        }

        additionalConfig()
    }

    client.plugin(HttpSend).intercept { request ->
        val path = request.url.encodedPath
        val requiresAuth = path.contains("/auth/")
        val access = tokenManager.loadAccessToken()

        if (requiresAuth && access.isBlank()) {
            sessionEvents.emit(SessionEvent.LoginRequired)
        }

        execute(request)
    }

    return client
}