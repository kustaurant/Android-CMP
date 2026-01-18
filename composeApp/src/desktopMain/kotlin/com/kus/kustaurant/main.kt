package com.kus.kustaurant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kus.data.firstLaunch.di.desktopFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import com.kus.logging.initLogger

fun main() {
    initLogger()
    initKoin(
        additionalModules = listOf(desktopFirstLaunchModule)
    )
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Kustaurant",
        ) {
            App()
        }
    }
}