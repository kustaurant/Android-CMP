package com.kus.feature.my.ui.webview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.MyPageTopBar
import com.kus.feature.my.component.WebView

@Composable
internal fun MyPageWebViewScreen(
    title: String,
    url: String,
    onBackClick: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF),
    ) {
        MyPageTopBar(
            title = title,
            onBackClick = onBackClick,
        )

        WebView(
            url = url,
            onLoadingChanged = { loading ->
                isLoading = loading
            },
            modifier = Modifier.weight(1f)
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
