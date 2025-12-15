package com.kus.kustaurant

import android.app.Application
import com.kus.data.firstLaunch.di.androidFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import org.koin.android.ext.koin.androidContext

class KusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin (
            config = {androidContext(this@KusApplication)},
            additionalModules = listOf(androidFirstLaunchModule)
        )
    }
}