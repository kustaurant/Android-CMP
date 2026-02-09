package com.kus.data.auth.di

import com.kus.data.auth.local.AndroidAuthPreferenceDataSource
import com.kus.data.auth.local.AuthPreferenceDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataAuthModule = module {
    single<AuthPreferenceDataSource> {
        AndroidAuthPreferenceDataSource(androidContext())
    }
}