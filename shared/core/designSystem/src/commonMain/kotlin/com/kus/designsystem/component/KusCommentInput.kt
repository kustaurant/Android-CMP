package com.kus.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_send
import org.jetbrains.compose.resources.painterResource

@Composable
fun KusCommentInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "댓글을 입력하세요",
    enabled: Boolean = true,
    autoFocus: Boolean = false,
    onSend: (String) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val canSend = enabled && value.isNotBlank()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = false,
            maxLines = 5,
            textStyle = KusTheme.typography.type14r.copy(
                color = KusTheme.colors.c_000000
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (canSend) {
                        keyboardController?.hide()
                        focusManager.clearFocus(true)
                        onSend(value.trim())
                    }
                }
            ),
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 45.dp, max = 113.dp)
                .background(
                    color = KusTheme.colors.c_EAEAEA,
                    shape = RoundedCornerShape(27.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth() ,
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = KusTheme.typography.type14r,
                            color = KusTheme.colors.c_AAAAAA
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(Modifier.width(6.dp))

        Surface(
            onClick = {
                if (canSend) {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                    onSend(value.trim())
                }
            },
            enabled = canSend,
            shape = CircleShape,
            color = KusTheme.colors.c_EAEAEA,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_send),
                    contentDescription = "전송",
                    tint = KusTheme.colors.c_43AB38,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}