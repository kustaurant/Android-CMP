package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_send
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailCommentInputBar(
    modifier: Modifier = Modifier,
    hasFocus: Boolean = false,
    onDismiss: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var commentText by remember { mutableStateOf("") }
    val placeholderText = "대댓글을 입력하세요"

    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0
    var hasKeyboardOpened by remember { mutableStateOf(isKeyboardVisible) }

    LaunchedEffect(hasFocus) {
        if (hasFocus) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            hasKeyboardOpened = true
        } else if (hasKeyboardOpened) {
            onDismiss()
        }
    }

    Row(
        modifier = modifier.padding(10.dp),
    ) {
        BasicTextField(
            value = commentText,
            onValueChange = { commentText = it },
            maxLines = 5,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .background(
                    shape = RoundedCornerShape(27.dp),
                    color = KusTheme.colors.c_EAEAEA
                ),
            textStyle = KusTheme.typography.type14r.copy(
                color = KusTheme.colors.c_323232
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (commentText.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = KusTheme.typography.type14r.copy(
                                color = KusTheme.colors.c_AAAAAA
                            )
                        )
                    }
                    innerTextField()
                }
            },
        )

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Box(
            modifier = Modifier.size(46.dp)
                .background(
                    shape = RoundedCornerShape(100.dp),
                    color = KusTheme.colors.c_EAEAEA
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_send),
                contentDescription = null,
                modifier = Modifier.padding(end = 2.dp)
            )
        }
    }
}
