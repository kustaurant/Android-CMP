package com.kus.domain.firstLaunch.di

import com.kus.domain.firstLaunch.usecase.GetFirstLaunchUseCase
import com.kus.domain.firstLaunch.usecase.PostFirstLaunchUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firstLaunchDomainModule = module {
    singleOf(::GetFirstLaunchUseCase)
    singleOf(::PostFirstLaunchUseCase)
}