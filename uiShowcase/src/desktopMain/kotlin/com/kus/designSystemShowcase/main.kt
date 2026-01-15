package com.kus.designSystemShowcase

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// ./gradlew :uiShowcase:run

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "DesignSystem Showcase") {
        MaterialTheme {
        }
    }
}
