package com.kus.data.community.di

import android.content.Context
import com.kus.data.community.AndroidPlatformImageResolver
import com.kus.data.community.PlatformImageResolver
import org.koin.dsl.module

val  androidDataCommunityModule = module {
    single<PlatformImageResolver> { AndroidPlatformImageResolver(get<Context>().applicationContext) }
}