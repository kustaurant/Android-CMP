package com.kus.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

expect fun provideEngine(): HttpClientEngine

fun createSharedHttpClient(): HttpClient =
    createHttpClient(engine = provideEngine())