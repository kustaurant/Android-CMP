package com.kus.data.login.di

import com.kus.data.login.api.AuthApi
import com.kus.data.login.repository.AuthRepositoryImpl
import com.kus.domain.login.repository.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::AuthApi)
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
}