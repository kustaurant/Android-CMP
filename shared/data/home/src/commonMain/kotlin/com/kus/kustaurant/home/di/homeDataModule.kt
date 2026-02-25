package com.kus.kustaurant.home.di

import com.kus.kustaurant.home.api.HomeApi
import com.kus.kustaurant.home.repositoryimpl.HomeRepositoryImpl
import com.kus.shared.domain.home.repository.HomeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeDataModule = module {
    singleOf(::HomeApi)
    singleOf(::HomeRepositoryImpl) bind HomeRepository::class
}
