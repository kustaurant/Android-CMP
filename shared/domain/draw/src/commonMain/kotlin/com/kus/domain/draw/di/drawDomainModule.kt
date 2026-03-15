package com.kus.domain.draw.di

import com.kus.domain.draw.usecase.GetDrawRestaurantUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val drawDomainModule = module {
    singleOf(::GetDrawRestaurantUseCase)
}