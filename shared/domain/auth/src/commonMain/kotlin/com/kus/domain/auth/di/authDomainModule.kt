package com.kus.domain.auth.di

import GetSessionAvailabilityUseCase
import com.kus.domain.auth.session.SessionEventBus
import com.kus.domain.auth.session.SessionEventEmitter
import com.kus.domain.auth.usecase.DeleteUserInfoUseCase
import com.kus.domain.auth.usecase.LogoutUseCase
import com.kus.domain.auth.usecase.PostNaverLoginUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

var authDomainModule = module {
    singleOf(::PostNaverLoginUseCase)
    singleOf(::DeleteUserInfoUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::GetSessionAvailabilityUseCase)
    single { SessionEventBus() }
    singleOf(::SessionEventBus) bind SessionEventEmitter::class
}