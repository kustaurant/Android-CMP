package com.kus.domain.auth.di

import com.kus.domain.auth.session.SessionEventBus
import com.kus.domain.auth.session.SessionEventEmitter
import com.kus.domain.auth.usecase.PostNaverLoginUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

var authDomainModule = module {
    singleOf(::PostNaverLoginUseCase)
    single { SessionEventBus() }
    singleOf(::SessionEventBus) bind SessionEventEmitter::class
}