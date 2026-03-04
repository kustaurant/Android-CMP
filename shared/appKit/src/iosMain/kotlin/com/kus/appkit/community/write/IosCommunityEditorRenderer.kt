package com.kus.appkit.community.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kus.feature.community.ui.write.CommunityEditorController
import com.kus.feature.community.ui.write.CommunityEditorRenderer

class IosCommunityEditorRenderer : CommunityEditorRenderer {
    @Composable
    override fun Render(
        controller: CommunityEditorController,
        modifier: Modifier,
        onHtmlChange: (String) -> Unit,
        onEditorReady: () -> Unit
    ) {
        IosCommunityEditor(
            controller = controller,
            modifier = modifier,
            onHtmlChange = onHtmlChange,
            onEditorReady = onEditorReady
        )
    }
}