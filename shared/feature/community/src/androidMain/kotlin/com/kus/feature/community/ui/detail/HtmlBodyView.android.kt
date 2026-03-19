package com.kus.feature.community.ui.detail

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kus.designsystem.theme.KusTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun HtmlBodyView(
    html: String,
    modifier: Modifier
) {
    val context = LocalContext.current

    val styledHtml =
        """
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
        </head>
        <body>
        $html
        </body>
        </html>
        """.trimIndent()

    AndroidView(
        modifier = modifier
            .padding(top = 0.dp)
            .border(1.dp, KusTheme.colors.c_43AB38),
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                null,
                styledHtml,
                "text/html",
                "utf-8",
                null
            )
        }
    )
}
