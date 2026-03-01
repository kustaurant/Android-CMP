package com.kus.feature.my.component

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun WebView(
    url: String,
    onLoadingChanged: (Boolean) -> Unit,
    modifier: Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {

                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(
                        view: WebView?,
                        url: String?,
                        favicon: Bitmap?
                    ) {
                        onLoadingChanged(true)
                    }

                    override fun onPageFinished(
                        view: WebView?,
                        url: String?
                    ) {
                        onLoadingChanged(false)
                    }
                }

                loadUrl(url)
            }
        }
    )
}