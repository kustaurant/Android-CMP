package com.kus.shared.data.tier.di

import com.kus.shared.data.tier.api.TierApi
import com.kus.shared.data.tier.repository.TierRepositoryImpl
import com.kus.shared.domain.tier.repository.TierRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tierDataModule = module {
    singleOf(::TierApi)
    singleOf(::TierRepositoryImpl) bind TierRepository::class
}