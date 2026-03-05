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
import com.kus.appkit.di.iosAuthModule
import com.kus.appkit.di.iosCommunityModule
import com.kus.appkit.di.iosTierMapPlatformModule
import com.kus.data.auth.di.iosAuthLocalModule
import com.kus.data.firstLaunch.di.iosFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import com.kus.logging.initLogger
import org.koin.core.KoinApplication

private var koinStarted = false
private var koinApp: KoinApplication? = null

fun MainViewController() = ComposeUIViewController {
    initLogger()
    if (!koinStarted) {
        koinApp = initKoin(
            additionalModules =
                listOf(
                    iosFirstLaunchModule,
                    iosTierMapPlatformModule,
                    iosAuthModule,
                    iosAuthLocalModule,
                    iosCommunityModule
                )
        )
        koinStarted = true
    }

    val koin = koinApp!!.koin
    //resolveOrLog(koin, PostNaverLoginUseCase::class)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        App()
    }
}
