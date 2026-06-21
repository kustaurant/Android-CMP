package com.kus.data.network

import com.kus.data.network.auth.TokenManager
import com.kus.domain.auth.session.SessionEventEmitter
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

class ApiClientProvider(
    private val engine: HttpClientEngine,
    private val tokenManager: TokenManager,
    private val sessionEvents: SessionEventEmitter,
    private val baseUrl: String,
    private val isDebug: Boolean = true,
) {
    private var _client: HttpClient = createClient()
    val client: HttpClient get() = _client

    fun reset() {
        _client.close()
        _client = createClient()
    }

    private fun createClient() = createApiHttpClient(
        engine = engine,
        tokenManager = tokenManager,
        sessionEvents = sessionEvents,
        baseUrl = baseUrl,
        isDebug = isDebug,
    )
}