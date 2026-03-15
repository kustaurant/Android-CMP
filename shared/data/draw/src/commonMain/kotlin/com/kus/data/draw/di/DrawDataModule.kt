package com.kus.data.draw.di

import com.kus.data.draw.api.DrawApi
import com.kus.data.draw.repository.DrawRepositoryImpl
import com.kus.domain.draw.repository.DrawRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DrawDataModule = module {
    singleOf(::DrawApi)
    singleOf(::DrawRepositoryImpl) bind DrawRepository::class
}