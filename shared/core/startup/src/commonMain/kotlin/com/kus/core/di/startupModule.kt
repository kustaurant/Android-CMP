package com.kus.core.di

import com.kus.core.startup.AppInitializer
import org.koin.dsl.module

val startupModule = module {
    single {
        AppInitializer(
            sessionBus = get(),
            apiClientProvider = get(),
        )
    }
}