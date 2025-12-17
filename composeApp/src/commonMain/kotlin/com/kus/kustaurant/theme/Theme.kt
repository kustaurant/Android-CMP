package com.kus.kustaurant.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

@Composable
internal expect fun platformDynamicColorScheme(
    darkTheme: Boolean = false,
): ColorScheme?

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF1E2024),
        onPrimary = White,
    )

@Composable
fun KusTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val dynamicScheme = if (dynamicColor) platformDynamicColorScheme(darkTheme) else null

    MaterialTheme(
        colorScheme = dynamicScheme ?: LightColorScheme,
        // TODO typography = KusTypography,
        content = content
    )
}
