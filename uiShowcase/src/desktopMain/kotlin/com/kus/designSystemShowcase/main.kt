package com.kus.designSystemShowcase

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kus.designSystemShowcase.component.DetailPreviewScreen
import com.kus.designSystemShowcase.component.EvaluatePreviewScreen
import com.kus.designsystem.theme.KusTheme

// ./gradlew :uiShowcase:run

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DesignSystem Showcase",
        state = rememberWindowState(
            width = 360.dp,
            height = 800.dp
        )
    ) {
        KusTheme {
            DetailPreviewScreen()
        }
    }
}
