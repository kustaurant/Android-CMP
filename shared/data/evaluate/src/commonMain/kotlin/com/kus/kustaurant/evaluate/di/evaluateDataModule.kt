package com.kus.kustaurant.evaluate.di

import com.kus.kustaurant.evaluate.api.EvaluateApi
import com.kus.kustaurant.evaluate.repositoryimpl.EvaluateRepositoryImpl
import com.kus.shared.domain.evaluate.repository.EvaluateRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val evaluateDataModule = module {
    singleOf(::EvaluateApi)
    singleOf(::EvaluateRepositoryImpl) bind EvaluateRepository::class
}
