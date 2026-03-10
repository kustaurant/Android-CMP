package com.kus.data.auth.di

import android.content.Context
import com.kus.data.auth.config.Keys
import com.kus.data.auth.local.AndroidAuthPreferenceDataSource
import com.kus.data.auth.local.AuthPreferenceDataSource
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataAuthModule = module {
    single<AuthPreferenceDataSource> {
        AndroidAuthPreferenceDataSource(androidContext())
    }

    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences(Keys.KEY_PREFERENCE, Context.MODE_PRIVATE)
        )
    }
}