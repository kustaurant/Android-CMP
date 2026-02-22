package com.kus.shared.domain.home.di

import com.kus.shared.domain.home.usecase.GetHomeInfoUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeDomainModule = module {
    singleOf(::GetHomeInfoUseCase)
}
