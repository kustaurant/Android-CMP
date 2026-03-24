package com.kus.feature.community.ui.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSBundle
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

private fun String.escapeForJsSingleQuote(): String {
    return this
        .replace("\\", "\\\\")
        .replace("'", "\\'")
        .replace("\n", "\\n")
        .replace("\r", "")
        .replace("</script>", "<\\/script>")
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun HtmlBodyView(
    html: String,
    modifier: Modifier
) {
    var webViewHeight by remember { mutableStateOf(1) }

    val styledHtml = remember(html) {
        val escapedHtml = html.escapeForJsSingleQuote()

        """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                * { box-sizing: border-box; }
                html { background-color: #ffffff !important; }
                body {
                    margin: 0;
                    padding: 0;
                    font-size: 16px;
                    color: #000000 !important;
                    background-color: #ffffff !important;
                    -webkit-text-fill-color: #000000 !important;
                    overflow: hidden;
                }
            </style>

            <script src="editor/purify.min.js"></script>
        </head>
        <body>
            <div id="content"></div>

            <script>
                const rawHtml = '$escapedHtml';
                const cleanHtml = DOMPurify.sanitize(rawHtml);
                document.getElementById('content').innerHTML = cleanHtml;
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    val delegate = remember {
        object : NSObject(), WKNavigationDelegateProtocol {
            override fun webView(
                webView: WKWebView,
                didFinishNavigation: WKNavigation?
            ) {
                webView.evaluateJavaScript("document.body.scrollHeight") { result, _ ->
                    val height = (result as? Double)?.toInt() ?: 0
                    if (height > 0) {
                        webViewHeight = height
                    }
                }
            }
        }
    }

    UIKitView(
        modifier = modifier
            .fillMaxWidth()
            .height(webViewHeight.dp),
        factory = {
            WKWebView(frame = CGRectMake(0.0, 0.0, 300.0, 300.0)).apply {
                navigationDelegate = delegate
                opaque = true
                backgroundColor = UIColor.whiteColor
                scrollView.backgroundColor = UIColor.whiteColor
                scrollView.scrollEnabled = false
                overrideUserInterfaceStyle = UIUserInterfaceStyle.UIUserInterfaceStyleLight
            }
        },
        update = { webView ->
            webView.loadHTMLString(
                styledHtml,
                baseURL = NSBundle.mainBundle.bundleURL
            )
        }
    )
}
