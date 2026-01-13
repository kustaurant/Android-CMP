package com.kus.kustaurant

import android.app.Application
import com.kus.data.firstLaunch.di.androidFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import com.kus.logging.initLogger
import org.koin.android.ext.koin.androidContext

class KusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()

        initKoin(
            config = { androidContext(this@KusApplication) },
            additionalModules = listOf(androidFirstLaunchModule)
        )
    }
}