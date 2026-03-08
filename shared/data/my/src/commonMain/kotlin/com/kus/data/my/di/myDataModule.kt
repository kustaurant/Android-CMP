package com.kus.data.my.di

import MyRepository
import com.kus.data.my.api.MyApi
import com.kus.data.my.api.MyCommunityApi
import com.kus.data.my.api.MyRestaurantApi
import com.kus.data.my.repositoryimpl.MyCommunityRepositoryImpl
import com.kus.data.my.repositoryimpl.MyRepositoryImpl
import com.kus.data.my.repositoryimpl.MyRestaurantRepositoryImpl
import com.kus.shared.domain.my.repository.MyCommunityRepository
import com.kus.shared.domain.my.repository.MyRestaurantRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val myDataModule = module {
    singleOf(::MyApi)
    singleOf(::MyCommunityApi)
    singleOf(::MyRestaurantApi)

    singleOf(::MyRepositoryImpl) bind MyRepository::class
    singleOf(::MyCommunityRepositoryImpl) bind MyCommunityRepository::class
    singleOf(::MyRestaurantRepositoryImpl) bind MyRestaurantRepository::class
}
