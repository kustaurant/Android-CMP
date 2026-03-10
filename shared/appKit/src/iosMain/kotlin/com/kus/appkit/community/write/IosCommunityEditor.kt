package com.kus.appkit.community.write

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.kus.feature.community.ui.write.CommunityEditorController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSBundle
import platform.Foundation.NSLog
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIColor
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.javaScriptEnabled
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
fun IosCommunityEditor(
    controller: CommunityEditorController,
    modifier: Modifier,
    onHtmlChange: (String) -> Unit,
    onEditorReady: () -> Unit
) {
    val editorDelegate = remember { mutableStateOf<IosEditorDelegate?>(null) }
    val onEditorReadyState = rememberUpdatedState(onEditorReady)

    val webView = remember {
        val config = WKWebViewConfiguration()
        val userController = WKUserContentController()

        userController.addScriptMessageHandler(
            scriptMessageHandler = HtmlChangedMessageHandler { html -> onHtmlChange(html) },
            name = "htmlChanged"
        )

        userController.addScriptMessageHandler(
            scriptMessageHandler = object : NSObject(), WKScriptMessageHandlerProtocol {
                override fun userContentController(
                    userContentController: WKUserContentController,
                    didReceiveScriptMessage: WKScriptMessage
                ) {
                    NSOperationQueue.mainQueue.addOperationWithBlock {
                        val delegate = editorDelegate.value
                        if (delegate != null) {
                            if (!delegate.isReady) {
                                delegate.onEditorReady()
                            } else {
                                delegate.onContentReady()
                            }
                        } else {
                            onEditorReadyState.value.invoke()
                        }
                    }
                }
            },
            name = "editorReady"
        )

        config.userContentController = userController
        config.preferences.javaScriptEnabled = true

        WKWebView(frame = CGRectZero.readValue(), configuration = config).also { wv ->
            wv.opaque = false
            wv.backgroundColor = UIColor.whiteColor
        }
    }

    LaunchedEffect(Unit) {
        val bundle = NSBundle.mainBundle
        val htmlUrl = bundle.URLForResource("quill", withExtension = "html") ?: run {
            NSLog("❌ quill.html not found"); return@LaunchedEffect
        }
        val baseUrl = bundle.resourceURL ?: run {
            NSLog("❌ bundle.resourceURL is null"); return@LaunchedEffect
        }

        val delegate = IosEditorDelegate(webView, controller)
        delegate.onContentReady = { onEditorReadyState.value.invoke() }
        editorDelegate.value = delegate
        controller.delegate = delegate

        webView.loadFileURL(URL = htmlUrl, allowingReadAccessToURL = baseUrl)
    }

    UIKitView(
        modifier = modifier,
        factory = { webView }
    )
}

private class HtmlChangedMessageHandler(
    private val onHtml: (String) -> Unit
) : NSObject(), WKScriptMessageHandlerProtocol {

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        val html = didReceiveScriptMessage.body.toString()
        onHtml(html)
    }
}
