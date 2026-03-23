package com.kus.feature.my.component

import androidx.compose.runtime.Composable
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
    var webViewRef: WKWebView? = null

    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView().apply {
                webViewRef = this

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

                loadRequest(
                    NSURLRequest.requestWithURL(NSURL.URLWithString(url)!!)
                )
            }
        },
        update = {
            webViewRef?.let {
                val currentUrl = it.URL?.absoluteString
                if (currentUrl != url) {
                    it.loadRequest(
                        NSURLRequest.requestWithURL(NSURL.URLWithString(url)!!)
                    )
                }
            }
        }
    )
}
