package com.kus.shared.data.search.di

import com.kus.shared.data.search.api.SearchApi
import com.kus.shared.data.search.repositoryimpl.SearchRepositoryImpl
import com.kus.shared.domain.search.repository.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::SearchApi)
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
}