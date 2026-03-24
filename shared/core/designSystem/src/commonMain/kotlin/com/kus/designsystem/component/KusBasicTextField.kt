package com.kus.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun KusBasicTextField(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    placeholder: String = "",
    singleLine: Boolean = true,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor = if (enabled) KusTheme.colors.c_000000 else KusTheme.colors.c_666666
    val backgroundColor = if (enabled) KusTheme.colors.c_FFFFFF else KusTheme.colors.c_E0E0E0

    Column(
        modifier =  modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = KusTheme.typography.type15sb,
            color = KusTheme.colors.c_323232,
        )

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_AAAAAA,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        Spacer(Modifier.height(4.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = KusTheme.typography.type15r.copy(textColor),
            enabled = enabled,
            interactionSource = interactionSource,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = KusTheme.colors.c_AAAAAA,
                    shape = RoundedCornerShape(12.dp),
                )
                .background(backgroundColor, RoundedCornerShape(12.dp)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = KusTheme.typography.type15r,
                            color = KusTheme.colors.c_EAEAEA
                        )
                    }

                    innerTextField()
                }
            }
        )
    }
}
