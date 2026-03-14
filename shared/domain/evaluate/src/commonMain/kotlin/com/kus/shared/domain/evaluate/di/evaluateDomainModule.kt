package com.kus.shared.domain.evaluate.di

import com.kus.shared.domain.evaluate.usecase.GetEvaluationUseCase
import com.kus.shared.domain.evaluate.usecase.PostEvaluationUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val evaluateDomainModule = module {
    singleOf(::GetEvaluationUseCase)
    singleOf(::PostEvaluationUseCase)
}
