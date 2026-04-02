package com.kus.feature.my.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ObjCSignatureOverride
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

@Composable
actual fun WebView(
    url: String,
    onLoadingChanged: (Boolean) -> Unit,
    modifier: Modifier,
) {
    val webView = remember {
        WKWebView().apply {
            navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {

                @ObjCSignatureOverride
                override fun webView(
                    webView: WKWebView,
                    didStartProvisionalNavigation: WKNavigation?
                ) {
                    onLoadingChanged(true)
                }

                @ObjCSignatureOverride
                override fun webView(
                    webView: WKWebView,
                    didFinishNavigation: WKNavigation?
                ) {
                    onLoadingChanged(false)
                }
            }
        }
    }

    LaunchedEffect(url) {
        webView.loadRequest(
            NSURLRequest.requestWithURL(NSURL.URLWithString(url)!!)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.stopLoading()
            webView.navigationDelegate = null
            webView.removeFromSuperview()
        }
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = { webView },
        update = { }
    )
}
