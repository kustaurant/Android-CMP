package com.kus.kustaurant.detail.di

import com.kus.kustaurant.detail.api.DetailApi
import com.kus.kustaurant.detail.repositoryimpl.DetailRepositoryImpl
import com.kus.shared.domain.detail.repository.DetailRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val detailDataModule = module {
    singleOf(::DetailApi)
    singleOf(::DetailRepositoryImpl) bind DetailRepository::class
}
