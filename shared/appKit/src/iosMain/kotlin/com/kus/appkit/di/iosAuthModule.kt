package com.kus.appkit.di

import com.kus.appkit.login.IosNaverAuthClient
import com.kus.feature.login.SocialAuthClient 
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val iosAuthModule = module {
    singleOf(::IosNaverAuthClient) bind SocialAuthClient::class
}