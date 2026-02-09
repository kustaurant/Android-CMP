package com.kus.data.network

import io.ktor.client.engine.HttpClientEngine

expect fun provideEngine(): HttpClientEngine
