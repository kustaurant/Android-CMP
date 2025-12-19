package com.kus.kustaurant

import androidx.compose.ui.window.ComposeUIViewController
import com.kus.data.firstLaunch.di.iosFirstLaunchModule
import com.kus.kustaurant.di.initKoin


fun MainViewController() = ComposeUIViewController {
    initKoin(
        additionalModules = listOf(iosFirstLaunchModule)
    )
    App()
}
