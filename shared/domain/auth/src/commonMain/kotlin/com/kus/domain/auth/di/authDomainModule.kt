package com.kus.domain.auth.di

import com.kus.domain.auth.session.SessionEventBus
import com.kus.domain.auth.session.SessionEventEmitter
import com.kus.domain.auth.usecase.DeleteUserInfoUseCase
import com.kus.domain.auth.usecase.DeleteUserTokensUseCase
import com.kus.domain.auth.usecase.GetSessionAvailabilityUseCase
import com.kus.domain.auth.usecase.LogoutUseCase
import com.kus.domain.auth.usecase.PostNaverLoginUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

var authDomainModule = module {
    singleOf(::PostNaverLoginUseCase)
    singleOf(::DeleteUserInfoUseCase)
    singleOf(::DeleteUserTokensUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::GetSessionAvailabilityUseCase)
    singleOf(::SessionEventBus) bind SessionEventEmitter::class
}