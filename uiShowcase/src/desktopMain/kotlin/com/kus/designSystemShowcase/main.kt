package com.kus.designSystemShowcase

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kus.feature.splash.ui.SplashScreen

// ./gradlew :designSystemShowcase:run

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "DesignSystem Showcase") {
        MaterialTheme {
            SplashScreen()
        }
    }
}
