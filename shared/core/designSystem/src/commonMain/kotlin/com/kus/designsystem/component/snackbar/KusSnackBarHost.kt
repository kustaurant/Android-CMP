package com.kus.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun KusSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            val shape = RoundedCornerShape(14.dp)
            Surface(
                shape = shape,
                tonalElevation = 0.dp,
                shadowElevation = 6.dp,
                color = KusTheme.colors.c_000000,
                contentColor = KusTheme.colors.c_FFFFFF,
            ) {
                Text(
                    text = data.visuals.message,
                    style = KusTheme.typography.type14r,
                    color = KusTheme.colors.c_FFFFFF,
                    modifier = Modifier
                        .clip(shape)
                        .background(KusTheme.colors.c_000000)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                )
            }
        }
    }
}