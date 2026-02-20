package com.kus.appkit.di

import com.kus.appkit.map.TierMapIosPlatform
import com.kus.feature.tier.ui.map.TierMapPlatform
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val iosTierMapPlatformModule = module {
    singleOf(::TierMapIosPlatform) bind TierMapPlatform::class
}