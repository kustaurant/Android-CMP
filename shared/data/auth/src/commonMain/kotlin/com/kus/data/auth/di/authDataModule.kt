package com.kus.data.auth.di

import com.kus.data.auth.TokenManagerImpl
import com.kus.data.auth.api.AuthApi
import com.kus.data.auth.api.AuthRefreshApi
import com.kus.data.auth.repository.AuthRepositoryImpl
import com.kus.data.auth.repository.AuthTokenStoreImpl
import com.kus.data.network.auth.TokenManager
import com.kus.domain.auth.repository.AuthRepository
import com.kus.domain.auth.repository.AuthTokenStore
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<AuthRefreshApi> {
        AuthRefreshApi(
            basicClient = get(named("basicClient")),
            baseUrl = get(named("BASE_URL"))
        )
    }

    single {
        AuthApi(
            basicClient = get(named("basicClient")),
            apiClient = get(named("apiClient")),
            baseUrl = get(named("BASE_URL"))
        )
    }
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::AuthTokenStoreImpl) bind AuthTokenStore::class
    singleOf(::TokenManagerImpl) bind TokenManager::class
}