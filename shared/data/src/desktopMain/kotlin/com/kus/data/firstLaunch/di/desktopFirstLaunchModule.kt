package com.kus.data.firstLaunch.di

import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import com.kus.data.firstLaunch.local.DesktopFirstLaunchLocalDataSource
import org.koin.dsl.module

val desktopFirstLaunchModule = module {
    single<FirstLaunchLocalDataSource> { DesktopFirstLaunchLocalDataSource() }
}