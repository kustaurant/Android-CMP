package com.kus.feature.community.ui.write

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class CommunityEditorController {

    var delegate: EditorDelegate? = null
    var expectsContent: Boolean = false

    fun setHtml(html: String) {
        expectsContent = true
        delegate?.setHtml(html)
    }
    
    fun insertImage(url: String) = delegate?.insertImage(url)
    fun undo() = delegate?.undo()
    fun redo() = delegate?.redo()
    fun toggleBold() = delegate?.toggleBold()
}

interface EditorDelegate {
    fun setHtml(html: String)
    fun insertImage(url: String)
    fun undo()
    fun redo()
    fun toggleBold()
}

@Composable
fun rememberCommunityEditorController(): CommunityEditorController =
    remember { CommunityEditorController() }