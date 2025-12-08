package com.kus.data.firstLaunch.di

import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import com.kus.data.firstLaunch.local.AndroidFirstLaunchLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val  androidFirstLaunchModule = module {
    single<FirstLaunchLocalDataSource> { AndroidFirstLaunchLocalDataSource(androidContext()) }
}
