package com.kus.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kus.designsystem.theme.KusTheme

@Composable
fun ReplyConfirmDialog(
    visible: Boolean,
    content: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "확인",
    dismissText: String = "취소",
) {
    if (!visible) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = KusTheme.colors.c_FFFFFF,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(min = 212.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = content,
                        style = KusTheme.typography.type13b,
                        color = KusTheme.colors.c_323232,
                    )
                }

                HorizontalDivider(
                    color = KusTheme.colors.c_E0E0E0,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            style = KusTheme.typography.type13m,
                            color = KusTheme.colors.c_43AB38
                        )
                    }

                    VerticalDivider(
                        color = KusTheme.colors.c_E0E0E0,
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onConfirm() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            style = KusTheme.typography.type13m,
                            color = KusTheme.colors.c_43AB38
                        )
                    }
                }
            }
        }
    }
}