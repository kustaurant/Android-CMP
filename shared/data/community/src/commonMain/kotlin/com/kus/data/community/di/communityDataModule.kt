package com.kus.data.community.di

import com.kus.data.community.api.CommunityApi
import com.kus.data.community.repository.CommunityRepositoryImpl
import com.kus.domain.community.repository.CommunityRepository
import okio.FileSystem
import okio.SYSTEM
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val communityDataModule = module {
    single<FileSystem> { FileSystem.SYSTEM }

    singleOf(::CommunityApi)
    singleOf(::CommunityRepositoryImpl) bind CommunityRepository::class
}