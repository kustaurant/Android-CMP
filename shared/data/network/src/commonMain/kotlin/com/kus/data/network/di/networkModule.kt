package com.kus.data.network.di

import com.kus.data.network.creatApiHttpClient
import com.kus.data.network.createBasicHttpClient
import com.kus.data.network.provideEngine
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named("basicClient")) {
        createBasicHttpClient(
            engine = provideEngine(),
            isDebug = true
        )
    }

    single(named("apiClient")) {
        creatApiHttpClient(
            engine = provideEngine(),
            tokenManager = get(),
            isDebug = true
        )
    }

    single<HttpClient> { get(named("apiClient")) }
}
