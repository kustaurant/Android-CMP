package com.kus.feature.community.ui.write

import android.webkit.WebView
import org.json.JSONObject

class AndroidEditorDelegate(
    private val webView: WebView
) : EditorDelegate {

    private fun eval(js: String) {
        webView.evaluateJavascript(js, null)
    }

    override fun setHtml(html: String) {
        val safe = JSONObject.quote(html)
        eval("window.EditorApi.setHtml($safe)")
    }

    override fun insertImage(url: String) {
        val safe = JSONObject.quote(url)
        eval("window.EditorApi.insertImage($safe)")
    }

    override fun undo() = eval("window.EditorApi.undo()")
    override fun redo() = eval("window.EditorApi.redo()")
    override fun toggleBold() = eval("window.EditorApi.toggleBold()")
}