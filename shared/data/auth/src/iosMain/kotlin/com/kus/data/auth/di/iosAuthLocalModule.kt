package com.kus.data.auth.di

import com.kus.data.auth.local.AuthPreferenceDataSource
import com.kus.data.auth.local.IosAuthPreferenceDataSource
import platform.Foundation.NSUserDefaults
import org.koin.dsl.module

val iosAuthLocalModule = module {
    single { NSUserDefaults.standardUserDefaults() }
    single<AuthPreferenceDataSource> { IosAuthPreferenceDataSource() }
}