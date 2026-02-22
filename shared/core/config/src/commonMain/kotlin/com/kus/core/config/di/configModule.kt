package com.kus.core.config.di

import com.kus.core.config.BuildKonfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val configModule = module {
    single(named("BASE_URL")) { BuildKonfig.API_BASE_URL }
}