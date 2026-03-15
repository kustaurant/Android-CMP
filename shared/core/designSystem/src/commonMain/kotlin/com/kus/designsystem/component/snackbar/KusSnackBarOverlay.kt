package com.kus.designsystem.component.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun KusSnackBarOverlay(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val bottomOffset = LocalSnackBarBottomPadding.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.ime)
            .padding(bottom = bottomOffset),
        contentAlignment = Alignment.BottomCenter
    ) {
        KusSnackBarHost(
            hostState = hostState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
