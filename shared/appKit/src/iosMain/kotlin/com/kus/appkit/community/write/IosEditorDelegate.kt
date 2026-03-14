package com.kus.appkit.community.write

import com.kus.feature.community.ui.util.jsQuote
import com.kus.feature.community.ui.write.CommunityEditorController
import com.kus.feature.community.ui.write.EditorDelegate
import platform.WebKit.WKWebView

class IosEditorDelegate(
    private val webView: WKWebView,
    private val controller: CommunityEditorController
) : EditorDelegate {

    var isReady = false
    private var pendingHtml: String? = null
    var onContentReady: (() -> Unit)? = null

    fun onEditorReady() {
        isReady = true
        val html = pendingHtml
        if (html != null) {
            eval("window.EditorApi.setHtml(${html.jsQuote()});")
            pendingHtml = null
        } else if (!controller.expectsContent) {
            onContentReady?.invoke()
        }
    }

    fun onContentReady() {
        onContentReady?.invoke()
    }

    private fun eval(js: String) {
        webView.evaluateJavaScript(js, completionHandler = null)
    }

    override fun setHtml(html: String) {
        if (isReady) {
            eval("window.EditorApi.setHtml(${html.jsQuote()});")
        } else {
            pendingHtml = html
        }
    }

    override fun insertImage(url: String) = eval("window.EditorApi.insertImage(${url.jsQuote()});")
    override fun undo() = eval("window.EditorApi.undo();")
    override fun redo() = eval("window.EditorApi.redo();")
    override fun toggleBold() = eval("window.EditorApi.toggleBold();")
}