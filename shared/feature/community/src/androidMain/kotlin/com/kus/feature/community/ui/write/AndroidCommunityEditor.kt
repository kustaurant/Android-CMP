@file:Suppress("SetJavaScriptEnabled")
package com.kus.feature.community.ui.write

import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AndroidCommunityEditor(
    controller: CommunityEditorController,
    modifier: Modifier,
    onHtmlChange: (String) -> Unit,
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun onHtmlChanged(html: String) {
                            onHtmlChange(html)
                        }
                    },
                    "AndroidBridge"
                )

                controller.delegate = AndroidEditorDelegate(this)

                val base = "composeResources"
                val pkg = context.assets.list(base)!!.first {
                    it.contains("community.generated.resources")
                }

                val path = "file:///android_asset/$base/$pkg/files/editor/quill.html"
                loadUrl(path)
            }
        },
        update = { webView ->
            controller.delegate = AndroidEditorDelegate(webView)
        }
    )
}

private class EditorBridge(
    private val onHtmlChanged: (String) -> Unit,
) {
    @JavascriptInterface
    fun onHtmlChanged(html: String) {
        onHtmlChanged(html)
    }
}