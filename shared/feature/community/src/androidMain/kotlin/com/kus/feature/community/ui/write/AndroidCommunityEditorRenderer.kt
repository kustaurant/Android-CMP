package com.kus.feature.community.ui.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class AndroidCommunityEditorRenderer : CommunityEditorRenderer {
    @Composable
    override fun Render(
        controller: CommunityEditorController,
        modifier: Modifier,
        onHtmlChange: (String) -> Unit,
        onEditorReady: () -> Unit
    ) {
        AndroidCommunityEditor(
            controller = controller,
            modifier = modifier,
            onHtmlChange = onHtmlChange,
            onEditorReady = onEditorReady
        )
    }
}