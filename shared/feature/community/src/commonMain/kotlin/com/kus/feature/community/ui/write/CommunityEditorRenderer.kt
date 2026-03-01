package com.kus.feature.community.ui.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface CommunityEditorRenderer {
    @Composable
    fun Render(
        controller: CommunityEditorController,
        modifier: Modifier,
        onHtmlChange: (String) -> Unit
    )
}