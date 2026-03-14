package com.kus.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable

@Composable
fun KusReplyCommentOverlay(
    visible: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    onSend: (String) -> Unit,
    onDismiss: () -> Unit,
    scrimAlpha: Float = 0.35f,
) {
    if (!visible) return

    Box(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(KusTheme.colors.c_000000.copy(alpha = scrimAlpha))
                .noRippleClickable { onDismiss() }
        )

        Surface(
            color = KusTheme.colors.c_FFFFFF,
            shadowElevation = 10.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                KusCommentInput(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = "대댓글을 입력하세요",
                    enabled = enabled,
                    autoFocus = true,
                    onSend = onSend,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}