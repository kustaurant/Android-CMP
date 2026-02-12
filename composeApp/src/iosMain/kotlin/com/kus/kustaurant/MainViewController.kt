package com.kus.kustaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeUIViewController
import com.kus.data.firstLaunch.di.iosFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import com.kus.logging.initLogger

fun MainViewController() = ComposeUIViewController {
    initLogger()

    initKoin(
        additionalModules = listOf(iosFirstLaunchModule)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        App()
    }
}
