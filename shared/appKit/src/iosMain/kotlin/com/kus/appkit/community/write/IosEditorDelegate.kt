package com.kus.appkit.community.write

import com.kus.feature.community.ui.util.jsQuote
import com.kus.feature.community.ui.write.EditorDelegate
import platform.WebKit.WKWebView

class IosEditorDelegate(
    private val webView: WKWebView
) : EditorDelegate {

    private var isReady = false
    private var pendingHtml: String? = null

    fun onEditorReady() {
        isReady = true
        pendingHtml?.let { html ->
            eval("window.EditorApi.setHtml(${html.jsQuote()});")
            pendingHtml = null
        }
    }

    private fun eval(js: String) {
        webView.evaluateJavaScript(js, completionHandler = null)
    }

    override fun setHtml(html: String) {
        if (isReady) {
            eval("window.EditorApi.setHtml(${html.jsQuote()});")
        } else {
            pendingHtml = html  // 준비될 때까지 보관
        }
    }

    override fun insertImage(url: String) = eval("window.EditorApi.insertImage(${url.jsQuote()});")
    override fun undo() = eval("window.EditorApi.undo();")
    override fun redo() = eval("window.EditorApi.redo();")
    override fun toggleBold() = eval("window.EditorApi.toggleBold();")
}