package com.kus.shared.domain.search.di

import com.kus.shared.domain.search.usecase.GetSearchResultUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val searchDomainModule = module {
    singleOf(::GetSearchResultUseCase)
}
