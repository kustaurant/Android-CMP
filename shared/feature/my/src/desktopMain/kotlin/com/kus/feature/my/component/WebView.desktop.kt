package com.kus.feature.my.component

import androidx.compose.runtime.LaunchedEffect
import java.awt.Desktop
import java.net.URI

@androidx.compose.runtime.Composable
actual fun WebView(
    url: String,
    onLoadingChanged: (Boolean) -> Unit,
    modifier: androidx.compose.ui.Modifier,
) {
    LaunchedEffect(Unit) {
        onLoadingChanged(true)
        Desktop.getDesktop().browse(URI(url))
        onLoadingChanged(false)
    }
}