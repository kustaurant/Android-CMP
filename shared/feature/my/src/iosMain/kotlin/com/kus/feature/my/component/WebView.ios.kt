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
    UIKitView(
        modifier = modifier,
        factory = {
            val webView = WKWebView()

            webView.navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {

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

            val request = NSURLRequest.requestWithURL(
                NSURL.URLWithString(url)!!
            )

            webView.loadRequest(request)

            webView
        }
    )
}