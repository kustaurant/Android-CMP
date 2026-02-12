package com.kus.data.network

import com.kus.core.config.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

expect fun provideEngine(): HttpClientEngine

fun createKtorClient(
    isDebug: Boolean = true,
    additionalConfig: io.ktor.client.HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    val baseUrl = BuildKonfig.API_BASE_URL
    return createHttpClient(
        engine = provideEngine(),
        baseUrl = baseUrl,
        isDebug = isDebug,
        additionalConfig = additionalConfig
    )
}
