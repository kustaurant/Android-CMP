package com.kus.data.firstLaunch.di

import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import com.kus.data.firstLaunch.local.IosFirstLaunchLocalDataSource
import org.koin.dsl.module

val iosFirstLaunchModule = module {
    single<FirstLaunchLocalDataSource> { IosFirstLaunchLocalDataSource() }
}