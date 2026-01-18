package com.kus.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideEngine(): HttpClientEngine =
    OkHttp.create()