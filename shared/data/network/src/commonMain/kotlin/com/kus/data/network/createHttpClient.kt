package com.kus.data.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(
    engine: HttpClientEngine,
    isDebug: Boolean = true,
    additionalConfig: HttpClientConfig<*>.() -> Unit = {}
): HttpClient = HttpClient(engine) {
    expectSuccess = true

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

    additionalConfig()
}