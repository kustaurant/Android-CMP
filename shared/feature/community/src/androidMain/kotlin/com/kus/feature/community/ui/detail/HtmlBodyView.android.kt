package com.kus.feature.community.ui.detail

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun HtmlBodyView(
    html: String,
    modifier: Modifier
) {
    val context = LocalContext.current

    val assetLoader = remember {
        WebViewAssetLoader.Builder()
            .addPathHandler(
                "/assets/",
                WebViewAssetLoader.AssetsPathHandler(context)
            )
            .build()
    }

    val customWebViewClient = remember {
        object : WebViewClientCompat() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }
        }
    }

    val escapedHtml = remember(html) {
        JSONObject.quote(html)
    }

    val wrappedHtml = remember(html) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                    background-color: transparent;
                }
            </style>
           <script src="https://appassets.androidplatform.net/assets/composeResources/kustaurant.shared.feature.community.generated.resources/files/editor/purify.min.js"></script>
        </head>
        <body>
            <div id="content"></div>

            <script>
                const dirtyHtml = $escapedHtml;
                const cleanHtml = DOMPurify.sanitize(dirtyHtml);
                document.getElementById("content").innerHTML = cleanHtml;
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    AndroidView(
        modifier = modifier.padding(top = 0.dp),
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                webViewClient = customWebViewClient
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                "https://appassets.androidplatform.net/",
                wrappedHtml,
                "text/html",
                "utf-8",
                null
            )
        }
    )
}