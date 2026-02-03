package com.kus.domain.login.di

import com.kus.domain.login.usecase.PostNaverLoginUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

var authDomainModule = module {
    singleOf(::PostNaverLoginUseCase)
}