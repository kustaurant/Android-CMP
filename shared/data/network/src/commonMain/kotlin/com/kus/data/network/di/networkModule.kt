package com.kus.data.network.di

import com.kus.data.network.createKtorClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        createKtorClient(
            isDebug = true,
        )
    }
}