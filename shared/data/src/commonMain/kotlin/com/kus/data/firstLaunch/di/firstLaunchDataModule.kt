package com.kus.data.firstLaunch.di

import com.kus.data.firstLaunch.repository.FirstLaunchRepositoryImpl
import com.kus.domain.firstLaunch.repository.FirstLaunchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firstLaunchDataModule = module {
    singleOf(::FirstLaunchRepositoryImpl) bind FirstLaunchRepository::class
}