package com.kus.appkit.community.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.community.ui.write.CommunityEditorController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSBundle
import platform.Foundation.NSLog
import platform.Foundation.NSURL
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
) {
    val editorDelegate = remember { mutableStateOf<IosEditorDelegate?>(null) }
    var isEditorReady by remember { mutableStateOf(false) }

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
                    editorDelegate.value?.onEditorReady()
                    isEditorReady = true
                }
            },
            name = "editorReady"
        )

        config.userContentController = userController
        config.preferences.javaScriptEnabled = true

        WKWebView(frame = CGRectZero.readValue(), configuration = config)
    }

    LaunchedEffect(Unit) {
        val bundle = NSBundle.mainBundle

        val htmlUrl: NSURL = bundle.URLForResource(
            name = "quill",
            withExtension = "html"
        ) ?: run {
            NSLog("❌ quill.html not found in bundle root")
            return@LaunchedEffect
        }

        val baseUrl: NSURL = bundle.resourceURL ?: run {
            NSLog("❌ bundle.resourceURL is null")
            return@LaunchedEffect
        }

        val delegate = IosEditorDelegate(webView)
        editorDelegate.value = delegate
        controller.delegate = delegate

        webView.loadFileURL(
            URL = htmlUrl,
            allowingReadAccessToURL = baseUrl
        )
    }

    Box(modifier = modifier.background(KusTheme.colors.c_FFFFFF)) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = { webView }
        )

        if (!isEditorReady) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KusTheme.colors.c_FFFFFF),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = KusTheme.colors.c_43AB38)
            }
        }
    }
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