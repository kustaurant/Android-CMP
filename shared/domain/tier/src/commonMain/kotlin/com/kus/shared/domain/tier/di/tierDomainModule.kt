package com.kus.shared.domain.tier.di

import com.kus.shared.domain.tier.usecase.GetTierRestaurantListUseCase
import com.kus.shared.domain.tier.usecase.GetTierRestaurantMapUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val tierDomainModule = module {
    singleOf(::GetTierRestaurantListUseCase)
    singleOf(::GetTierRestaurantMapUseCase)
}