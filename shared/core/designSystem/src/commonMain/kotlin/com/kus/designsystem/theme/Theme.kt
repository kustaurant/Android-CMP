package com.kus.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalKusColors = staticCompositionLocalOf<KusColors> {
    error("No KusColors provided")
}

private val LocalKusTypography = staticCompositionLocalOf<KusTypography> {
    error("No KusTypography provided")
}

object KusTheme {
    val colors: KusColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKusColors.current
    val typography: KusTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKusTypography.current
}

@Composable
fun ProvideKusColorsAndTypography(
    colors: KusColors,
    typography: KusTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }.apply { update(colors) }
    CompositionLocalProvider(
        LocalKusColors provides provideColors,
        LocalKusTypography provides typography,
        content = content
    )
}

@Composable
fun KusTheme(
    content: @Composable () -> Unit
) {
    val colors = KusLightColors()
    val typography = createKusTypography()

    ProvideKusColorsAndTypography(
        colors = colors,
        typography = typography
    ) {
        MaterialTheme(
            content = content
        )
    }
}
