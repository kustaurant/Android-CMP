package com.kus.shared.domain.detail.di

import com.kus.shared.domain.detail.usecase.GetRestaurantDetailUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailDomainModule = module {
    singleOf(::GetRestaurantDetailUseCase)
}
