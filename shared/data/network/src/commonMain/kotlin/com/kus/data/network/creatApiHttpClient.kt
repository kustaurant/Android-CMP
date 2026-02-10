package com.kus.data.network

import com.kus.data.network.auth.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun creatApiHttpClient(
    engine: HttpClientEngine,
    tokenManager: TokenManager,
    isDebug: Boolean = true,
    additionalConfig: HttpClientConfig<*>.() -> Unit = {}
): HttpClient = HttpClient(engine = engine) {
    expectSuccess = false

    if (isDebug) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                isLenient = true
            }
        )
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
                path.startsWith("/api/v2/login/naver") ||
                        path.startsWith("/api/v2/token/refresh")
            }

            loadTokens {
                tokenManager.loadAccessToken()
                    .takeIf {it.isNotBlank() }
                    ?.let { access ->
                    BearerTokens(accessToken = access, refreshToken = "")
                }
            }

            refreshTokens {
                tokenManager.refreshAndGetNewAccessToken()
                    .takeIf{it.isNotBlank()}
                    ?.let { newAccess ->
                    BearerTokens(accessToken = newAccess, refreshToken = "")
                }
            }
        }
    }

    additionalConfig()
}