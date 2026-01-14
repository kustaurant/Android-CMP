package com.kus.kustaurant

import androidx.compose.ui.window.ComposeUIViewController
import com.kus.data.firstLaunch.di.iosFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import com.kus.logging.initLogger


fun MainViewController() = ComposeUIViewController {
    initLogger()

    initKoin(
        additionalModules = listOf(iosFirstLaunchModule)
    )
    App()
}
